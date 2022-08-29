package br.com.palota.cinema;

import br.com.palota.cinema.dao.DAO;
import br.com.palota.cinema.dao.SalaDao;
import br.com.palota.cinema.model.Sala;
import br.com.palota.cinema.service.SalaService;
import br.com.palota.cinema.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroSala;

    @FXML
    private MenuItem menuItemCadastroFilme;

    @FXML
    private MenuItem menuItemCadastroSessao;

    @FXML
    private MenuItem menuItemAjusaSobre;

    @FXML
    protected void onMenuItemCadastroSalaAction() {
        loadView("sala-lista-view.fxml", (SalaListController controller) -> {
            controller.setSalaDao(new SalaService(new SalaDao()));
            controller.updateTableView();
        });
    }

    @FXML
    protected void onMenuItemCadastroFilmeAction() {

    }

    @FXML
    protected void onMenuItemCadastroSessaoAction() {

    }

    @FXML
    protected void onMenuItemAjudaSobreAction() {
        loadView("sobre-view.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load();
            Scene mainScene = MainApplication.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVbox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);
        } catch (IOException e) {
            Alerts.showAlerts("IOException", "Erro ao carregar a janela", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}