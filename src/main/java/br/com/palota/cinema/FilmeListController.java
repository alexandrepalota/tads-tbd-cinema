package br.com.palota.cinema;

import br.com.palota.cinema.dao.FilmeDao;
import br.com.palota.cinema.dao.FilmeDao;
import br.com.palota.cinema.model.Filme;
import br.com.palota.cinema.model.Filme;
import br.com.palota.cinema.model.Filme;
import br.com.palota.cinema.service.FilmeService;
import br.com.palota.cinema.service.FilmeService;
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
import javafx.scene.control.ButtonType;
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

public class FilmeListController implements Initializable, DataChangeListener {

    private FilmeService service;

    @FXML
    private TableView<Filme> tableViewFilme;

    @FXML
    private TableColumn<Filme, Long> tableColumnId;

    @FXML
    private TableColumn<Filme, String> tableColumnTitulo;

    @FXML
    private TableColumn<Filme, String> tableColumnGenero;

    @FXML
    private TableColumn<Filme, String> tableColumnSinopse;

    @FXML
    private TableColumn<Filme, Filme> tableColumnRemover;

    @FXML
    private TableColumn<Filme, Filme> tableColumnEditar;

    @FXML
    private Button buttonNovo;

    private ObservableList<Filme> obsList;

    public void setService(FilmeService service) {
        this.service = service;
    }

    @FXML
    protected void onButtonNovoAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Filme filme = new Filme();
        createDialogForm(filme, "filme-formulario-view.fxml", parentStage);
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tableColumnSinopse.setCellValueFactory(new PropertyValueFactory<>("sinopse"));
        tableColumnGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // Fazendo o TableView acompanhar a altura da janela
        Stage stage = (Stage) MainApplication.getMainScene().getWindow();
        tableViewFilme.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service nulo");
        }
        List<Filme> lista = service.buscarTodos();
        obsList = FXCollections.observableArrayList(lista);
        tableViewFilme.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Filme filme, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            FilmeFormController controller = loader.getController();
            controller.setFilme(filme);
            controller.setService(new FilmeService(new FilmeDao()));
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Insira os dados do Filme");
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
        tableColumnEditar.setCellFactory(param -> new TableCell<Filme, Filme>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Filme obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(
                        obj, "filme-formulario-view.fxml", Utils.currentStage(event)
                ));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemover.setCellFactory(param -> new TableCell<Filme, Filme>() {
            private final Button button = new Button("Excluir");
            @Override
            protected void updateItem(Filme obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Filme filme) {
        var result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja exluir?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service nulo");
            }
            try {
                service.excluir(filme.getId());
                updateTableView();
            } catch (Exception e) {
                Alerts.showAlerts("Erro ao excluir", "Não foi possível excluir", "Verifique se não há sessões associadas a este filme", Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }
}
