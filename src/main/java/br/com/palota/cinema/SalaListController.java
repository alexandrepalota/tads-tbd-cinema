package br.com.palota.cinema;

import br.com.palota.cinema.dao.SalaDao;
import br.com.palota.cinema.model.Sala;
import br.com.palota.cinema.service.SalaService;
import br.com.palota.cinema.util.Alerts;
import br.com.palota.cinema.util.DataChangeListener;
import br.com.palota.cinema.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalaListController implements Initializable, DataChangeListener {

    private SalaService salaService;

    @FXML
    private TableView<Sala> tableViewSala;

    @FXML
    private TableColumn<Sala, Long> tableColumnId;

    @FXML
    private TableColumn<Sala, String> tableColumnNome;

    @FXML
    private TableColumn<Sala, Integer> tableColumnCapacidade;

    @FXML
    private TableColumn<Sala, Sala> tableColumnEditar;

    @FXML
    private Button buttonNovo;

    private ObservableList<Sala> obsList;

    @FXML
    protected void onButtonNovoAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Sala sala = new Sala();
        createDialogForm(sala, "sala-formulario-view.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void setSalaDao(SalaService service) {
        this.salaService = service;
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));

        // Fazendo o TableView acompanhar a altura da janela
        Stage stage = (Stage) MainApplication.getMainScene().getWindow();
        tableViewSala.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (salaService == null) {
            throw new IllegalStateException("Service nulo");
        }
        List<Sala> lista = salaService.buscarTodos();
        obsList = FXCollections.observableArrayList(lista);
        tableViewSala.setItems(obsList);
        initEditButtons();
    }

    private void createDialogForm(Sala sala, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            SalaFormController controller = loader.getController();
            controller.setSala(sala);
            controller.setService(new SalaService(new SalaDao()));
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados da Sala");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlerts("IOException", "Erro ao carregar a janela", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initEditButtons() {
        tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEditar.setCellFactory(param -> new TableCell<Sala, Sala>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Sala obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(
                        obj, "sala-formulario-view.fxml", Utils.currentStage(event)
                ));
            }
        });
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
