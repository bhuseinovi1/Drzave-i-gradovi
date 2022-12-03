package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GradController {
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;

    public ChoiceBox<Drzava> choiceDrzava;
    public ObservableList<Drzava> listDrzave;
    public ObservableList<String> listTipovi;
    public ObservableList<Grad> listGradovi;
    public ObservableList<Grad> listPobratimi;
    public TextField fieldNadmorska;
    public Slider sliderZagadjenost;
    public ChoiceBox<String> choiceTipGrada;
    public ListView<Grad> listViewPobratimi;
    public ChoiceBox<Grad> choiceGrad;
    public ImageView imageView;
    private Grad grad;

    private String imagePath;

    public GradController(Grad grad, ArrayList<Drzava> drzave) {
        this.grad = grad;
        listDrzave = FXCollections.observableArrayList(drzave);
        listTipovi = FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList("Razvijeni grad","Srednje razvijeni grad","Nerazvijeni grad")));
    }

    public GradController(Grad grad, ArrayList<Drzava> drzave, ArrayList<Grad> gradovi) {
        this.grad = grad;
        listDrzave = FXCollections.observableArrayList(drzave);
        listTipovi = FXCollections.observableArrayList(new ArrayList<String>(Arrays.asList("Razvijeni grad","Srednje razvijeni grad","Nerazvijeni grad")));
        listGradovi = FXCollections.observableArrayList(gradovi);
        if(grad!=null)
            listPobratimi = FXCollections.observableArrayList(grad.getPobratimi());
        else
            listPobratimi = FXCollections.observableArrayList(new ArrayList<Grad>());
        if(grad!=null) imagePath = grad.getSlika();
    }

    @FXML
    public void initialize() {

        choiceDrzava.setItems(listDrzave);
        choiceTipGrada.setItems(listTipovi);
        choiceGrad.setItems(listGradovi);

        if (grad != null) {
            fieldNaziv.setText(grad.getNaziv());
            fieldNadmorska.setText(Integer.toString(grad.getNadmorskaVisina()));
            fieldBrojStanovnika.setText(Integer.toString(grad.getBrojStanovnika()));
            sliderZagadjenost.setValue(grad.getZagadjenost());
            // choiceDrzava.getSelectionModel().select(grad.getDrzava());
            // ovo ne radi jer grad.getDrzava() nije identički jednak objekat kao član listDrzave
            for (Drzava drzava : listDrzave) {
                if (drzava.getId() == grad.getDrzava().getId()) {
                    choiceDrzava.getSelectionModel().select(drzava);
                }
            }

            listViewPobratimi.setItems(listPobratimi);
//            try {
//                boolean slikaNedostupna = true;
////                imageView.setImage(new Image(new FileInputStream(grad.getSlika())));
//                    throw new FileNotFoundException();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            //dodano
            if(grad instanceof RazvijeniGrad) choiceTipGrada.getSelectionModel().select(0);
            else if(grad instanceof  SrednjeRazvijeniGrad) choiceTipGrada.getSelectionModel().select(1);
            else choiceTipGrada.getSelectionModel().select(2);
        } else {
            choiceDrzava.getSelectionModel().selectFirst();
            choiceTipGrada.getSelectionModel().selectFirst();
        }
        choiceGrad.getSelectionModel().selectFirst();
    }

    public Grad getGrad() {
        return grad;
    }

    public void clickCancel(ActionEvent actionEvent) {
        grad = null;
        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
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


        int brojStanovnika = 0;
        try {
            brojStanovnika = Integer.parseInt(fieldBrojStanovnika.getText());
        } catch (NumberFormatException e) {
            // ...
        }
        if (brojStanovnika <= 0) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
        }

        int nadmorskaVisina=0;
        try {
            nadmorskaVisina = Integer.parseInt(fieldNadmorska.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if(nadmorskaVisina<-400 || nadmorskaVisina>8000) {
            fieldNadmorska.getStyleClass().removeAll("poljeIspravno");
            fieldNadmorska.getStyleClass().add("poljeNijeIspravno");
            sveOk = false;
        } else {
            fieldNadmorska.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNadmorska.getStyleClass().add("poljeIspravno");
        }

        if (!sveOk) return;

//        if (grad == null) {
//            if(choiceTipGrada.getValue().equals("Razvijeni grad")) grad = new RazvijeniGrad();
//            else if(choiceTipGrada.getValue().equals("Nerazvijeni grad")) grad = new NerazvijeniGrad();
//            else grad=new SrednjeRazvijeniGrad();
//            //grad = new Grad();
//        }


        //dodano
        int id=0;
        if(grad!=null) id=grad.getId();
        if(choiceTipGrada.getValue().equals("Razvijeni grad")) grad = new RazvijeniGrad();
        else if(choiceTipGrada.getValue().equals("Nerazvijeni grad")) grad = new NerazvijeniGrad();
        else grad=new SrednjeRazvijeniGrad();
        if(id!=0) grad.setId(id); // u slucaju da se vrse promjene nad postojecim gradom


        grad.setNaziv(fieldNaziv.getText());
        grad.setBrojStanovnika(Integer.parseInt(fieldBrojStanovnika.getText()));
        grad.setNadmorskaVisina(Integer.parseInt(fieldNadmorska.getText()));
        grad.setDrzava(choiceDrzava.getValue());
        grad.setZagadjenost((int) sliderZagadjenost.getValue());

        // dodati setter za sliku
        grad.setSlika(imagePath);

        //bitno
        grad.setPobratimi(new ArrayList<Grad>(listViewPobratimi.getItems()));
        for(Grad twoWayPobratim : listViewPobratimi.getItems()) {
            twoWayPobratim.getPobratimi().add(grad);
        }

        Stage stage = (Stage) fieldNaziv.getScene().getWindow();
        stage.close();
    }

    public void clickDodaj(ActionEvent actionEvent) {
        if(grad!=null) {
            if(choiceGrad.getSelectionModel().getSelectedItem().getId()==grad.getId()) return;
        }
        if(!listViewPobratimi.getItems().contains(choiceGrad.getSelectionModel().getSelectedItem())) {
            listViewPobratimi.getItems().add(choiceGrad.getSelectionModel().getSelectedItem());
        }
    }

    public void promijeniSliku(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretraga.fxml"));
            PretragaController pretragaController = new PretragaController(imagePath);
            loader.setController(pretragaController);
            root = loader.load();
            stage.setTitle("Pretraga datoteka");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                imagePath = pretragaController.getImagePath();
                if(imagePath!=null) {
                    try {
                        imageView.setImage(new Image(new FileInputStream(imagePath)));
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
