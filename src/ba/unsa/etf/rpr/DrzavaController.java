package ba.unsa.etf.rpr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DrzavaController {
    public TextField fieldNaziv;
    public ChoiceBox<Grad> choiceGrad;
    public ChoiceBox<Grad> choiceGradNajveci;
    public RadioButton radioDrugi;
    public RadioButton radioIsti;
    private Drzava drzava;
    private ObservableList<Grad> listGradoviGlavni;
    private ObservableList<Grad> listGradoviNajveci;

    public DrzavaController(Drzava drzava, ArrayList<Grad> gradovi) {
        this.drzava = drzava;
        listGradoviGlavni = FXCollections.observableArrayList(gradovi);
        listGradoviNajveci = FXCollections.observableArrayList(gradovi);
    }

    public void checkiranjeRadioButtona(boolean isti) {
        radioIsti.setSelected(isti);
        radioDrugi.setSelected(!isti);
        choiceGradNajveci.setDisable(isti);

        if(isti) choiceGradNajveci.getSelectionModel().select(choiceGrad.getValue());
    }

    @FXML
    public void initialize() {

        // listener koji za svaku promjenu glavnog grada, mijenja i najveći grad u slucaju da su isti
        choiceGrad.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Grad>() {
            @Override
            public void changed(ObservableValue<? extends Grad> observableValue, Grad grad, Grad t1) {
                if(radioIsti.isSelected()) choiceGradNajveci.getSelectionModel().select(t1);
            }
        });

        choiceGrad.setItems(listGradoviGlavni);
        choiceGradNajveci.setItems(listGradoviNajveci);
        if (drzava != null) {
            if(drzava.getGlavniGrad().getId()==drzava.getNajveciGrad().getId()) {
                checkiranjeRadioButtona(true);
            }
            else {
                checkiranjeRadioButtona(false);
            }
            fieldNaziv.setText(drzava.getNaziv());
            choiceGrad.getSelectionModel().select(drzava.getGlavniGrad());
            choiceGradNajveci.getSelectionModel().select(drzava.getNajveciGrad());
        } else {
            checkiranjeRadioButtona(true);
            choiceGrad.getSelectionModel().selectFirst();
        }
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void clickOk(ActionEvent actionEvent) {
        boolean sveOk = true;

        if (fieldNaziv.getText().trim().isEmpty()) {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        }

        if (!sveOk) return;

        if (drzava == null) drzava = new Drzava();
        drzava.setNaziv(fieldNaziv.getText());
        drzava.setGlavniGrad(choiceGrad.getSelectionModel().getSelectedItem());
        drzava.setNajveciGrad(choiceGradNajveci.getSelectionModel().getSelectedItem());
        closeWindow();
    }

    public void clickCancel(ActionEvent actionEvent) {
        drzava = null;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }

    public void onRazlicit(ActionEvent actionEvent) {
        checkiranjeRadioButtona(false);
    }

    public void onIsti(ActionEvent actionEvent) {
        checkiranjeRadioButtona(true);
    }
}
