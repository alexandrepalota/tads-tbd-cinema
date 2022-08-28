package br.com.palota.cinema;

import br.com.palota.cinema.model.Sala;
import br.com.palota.cinema.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastrarSala;

    @FXML
    private MenuItem menuItemCadastrarFilme;

    @FXML
    private MenuItem menuItemCadastrarSessao;

    @FXML
    private MenuItem menuItemAjusaSobre;

    @FXML
    protected void onMenuItemCadastrarSalaAction() {

    }

    @FXML
    protected void onMenuItemCadastrarFilmeAction() {

    }

    @FXML
    protected void onMenuItemCadastrarSessaoAction() {

    }

    @FXML
    protected void onMenuItemAjudaSobreAction() {
        loadView("sobre-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private synchronized void loadView(String absoluteName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load();
            Scene mainScene = MainApplication.getMainScene();
            // Obtendo referência para o VBox principal
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            // Guardando a referência para o menu principal
            Node mainMenu = mainVbox.getChildren().get(0);
            // Limpando o VBox antigo
            mainVbox.getChildren().clear();
            // Adicionando o menu principal
            mainVbox.getChildren().add(mainMenu);
            // Adicionando os filhos do novo VBox
            mainVbox.getChildren().addAll(newVbox.getChildren());
        } catch (IOException e) {
            Alerts.showAlerts("IOException", "Erro ao carregar a janela", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}