package br.com.palota.cinema;

import br.com.palota.cinema.model.Sala;
import br.com.palota.cinema.dao.SalaDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalaListController implements Initializable {

    private SalaDao salaDao;

    @FXML
    private TableView<Sala> tableViewSala;

    @FXML
    private TableColumn<Sala, Long> tableColumnId;

    @FXML
    private TableColumn<Sala, String> tableColumnNome;

    @FXML
    private TableColumn<Sala, Integer> tableColumnCapacidade;

    @FXML
    private Button buttonNovo;

    private ObservableList<Sala> obsList;

    @FXML
    protected void onButtonNovoAction() {
        System.out.println("Foi clicado");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void setSalaDao(SalaDao dao) {
        this.salaDao = dao;
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
        if (salaDao == null) {
            throw new IllegalStateException("Dao nulo");
        }
        List<Sala> lista = salaDao.findAll();
        obsList = FXCollections.observableArrayList(lista);
        tableViewSala.setItems(obsList);
    }
}
