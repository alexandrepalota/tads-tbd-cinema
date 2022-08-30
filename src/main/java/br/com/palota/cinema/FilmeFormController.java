package br.com.palota.cinema;

import br.com.palota.cinema.exception.ValidationException;
import br.com.palota.cinema.model.Filme;
import br.com.palota.cinema.service.FilmeService;
import br.com.palota.cinema.util.Alerts;
import br.com.palota.cinema.util.Constraints;
import br.com.palota.cinema.util.DataChangeListener;
import br.com.palota.cinema.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class FilmeFormController implements Initializable {

    private Filme filme;
    private FilmeService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextArea txtSinopse;

    @FXML
    private Label lblTituloErro;

    @FXML
    private Label lblGeneroErro;

    @FXML
    private Label lblSinopseErro;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public void setService(FilmeService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSalvarAction(ActionEvent event) {
        if (filme == null) {
            throw new IllegalStateException("Filme nulo");
        }
        if (service == null) {
            throw new IllegalStateException("Service nulo");
        }
        try {
            this.filme = getFormData();
            service.salvar(this.filme);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (Exception e) {
            Alerts.showAlerts("Erro ao salvar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnCancelarAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtTitulo, 60);
        Constraints.setTextFieldMaxLength(txtGenero, 20);
        Constraints.setTextAreadMaxLength(txtSinopse, 255);
    }

    public void updateFormData() {
        if (filme == null) {
            throw new IllegalStateException("Filme nulo");
        }
        txtId.setText(String.valueOf(filme.getId()));
        txtTitulo.setText(filme.getTitulo());
        txtGenero.setText(filme.getGenero());
        txtSinopse.setText(filme.getSinopse());
    }

    private Filme getFormData() {
        Filme filme = new Filme();
        ValidationException exception = new ValidationException("Erros de validação");
        filme.setId(Utils.tryParseLong(txtId.getText()));

        if (txtTitulo.getText() == null || txtTitulo.getText().trim().equals("")) {
            exception.addError("titulo", "Titulo não pode ser vazio");
        }
        filme.setTitulo(txtTitulo.getText());

        if (txtGenero.getText() == null || txtGenero.getText().trim().equals("")) {
            exception.addError("genero", "Genero não pode ser vazio");
        }
        filme.setGenero(txtGenero.getText());

        if (txtSinopse.getText() == null || txtSinopse.getText().trim().equals("")) {
            exception.addError("sinopse", "Sinopse não pode ser vazio");
        }
        filme.setSinopse(txtSinopse.getText());

        if (exception.getErrors().size() > 0) throw exception;

        return filme;
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener l : dataChangeListeners) {
            l.onDataChanged();
        }
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        if (fields.contains("titulo")) lblTituloErro.setText(errors.get("titulo"));
        if (fields.contains("genero")) lblGeneroErro.setText(errors.get("genero"));
        if (fields.contains("sinopse")) lblSinopseErro.setText(errors.get("sinopse"));
    }
}
