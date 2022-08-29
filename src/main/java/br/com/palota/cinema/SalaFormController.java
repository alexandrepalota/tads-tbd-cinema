package br.com.palota.cinema;

import br.com.palota.cinema.exception.ValidationException;
import br.com.palota.cinema.model.Sala;
import br.com.palota.cinema.service.SalaService;
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
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class SalaFormController implements Initializable {

    private Sala sala;
    private SalaService service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCapacidade;

    @FXML
    private Label lblNomeErro;

    @FXML
    private Label lblCapacidadeErro;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public void setService(SalaService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSalvarAction(ActionEvent event) {
        if (sala == null) {
            throw new IllegalStateException("Sala nula");
        }
        if (service == null) {
            throw new IllegalStateException("Service nulo");
        }
        try {
            this.sala = getFormData();
            service.salvar(this.sala);
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
        Constraints.setTextFieldInteger(txtCapacidade);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }

    public void updateFormData() {
        if (sala == null) {
            throw new IllegalStateException("Sala nula");
        }
        txtId.setText(String.valueOf(sala.getId()));
        txtNome.setText(sala.getNome());
        txtCapacidade.setText(String.valueOf(sala.getCapacidade()));
    }

    private Sala getFormData() {
        Sala sala = new Sala();
        ValidationException exception = new ValidationException("Erros de validação");
        sala.setId(Utils.tryParseLong(txtId.getText()));

        if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
            exception.addError("nome", "Nome não pode ser vazio");
        }
        sala.setNome(txtNome.getText());

        if (txtCapacidade.getText() == null || txtCapacidade.getText().trim().equals("") || Utils.tryParseInt(txtCapacidade.getText()) <= 0) {
            exception.addError("capacidade", "Capacidade deve ser maior que zero");
        }
        sala.setCapacidade(Utils.tryParseInt(txtCapacidade.getText()));

        if (exception.getErrors().size() > 0) throw exception;

        return sala;
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener l : dataChangeListeners) {
            l.onDataChanged();
        }
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        if (fields.contains("nome")) lblNomeErro.setText(errors.get("nome"));
        if (fields.contains("capacidade")) lblCapacidadeErro.setText(errors.get("capacidade"));
    }
}
