package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {

    public TableView<Grad> tableViewGradovi;
    public TableColumn colGradId;
    public TableColumn colGradNaziv;
    public TableColumn colGradStanovnika;
    public TableColumn colGradNadmorska;
    public TableColumn<Grad,String> colGradDrzava;
    public TableColumn colGradZagadjenost;
    public TableColumn<Grad,Integer> colBrojBolnica;
    private GeografijaDAO dao;
    private ObservableList<Grad> listGradovi;

    public GlavnaController() {
        dao = GeografijaDAO.getInstance();
        listGradovi = FXCollections.observableArrayList(dao.gradovi());
    }

    @FXML
    public void initialize() {
        tableViewGradovi.setItems(listGradovi);
        colGradId.setCellValueFactory(new PropertyValueFactory("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory("brojStanovnika"));
        colGradNadmorska.setCellValueFactory(new PropertyValueFactory("nadmorskaVisina"));
        colGradZagadjenost.setCellValueFactory(new PropertyValueFactory("zagadjenost"));
        colBrojBolnica.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().brojBolnica()).asObject());
        colGradDrzava.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrzava().getNaziv()));
    }

    public void actionDodajGrad(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
            //GradController gradController = new GradController(null, dao.drzave());
            GradController gradController = new GradController(null, dao.drzave(),dao.gradovi());
            loader.setController(gradController);
            root = loader.load();
            stage.setTitle("Grad");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Grad grad = gradController.getGrad();
                if (grad != null) {
                    dao.dodajGrad(grad);
                    listGradovi.setAll(dao.gradovi());
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void actionDodajDrzavu(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
            DrzavaController drzavaController = new DrzavaController(null, dao.gradovi());
            loader.setController(drzavaController);
            root = loader.load();
            stage.setTitle("Država");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Drzava drzava = drzavaController.getDrzava();
                if (drzava != null) {
                    dao.dodajDrzavu(drzava);
                    listGradovi.setAll(dao.gradovi());
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionIzmijeniGrad(ActionEvent actionEvent) {
        Grad grad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (grad == null) return;

        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
            //GradController gradController = new GradController(grad, dao.drzave());
            GradController gradController = new GradController(grad, dao.drzave(),dao.gradovi());
            loader.setController(gradController);
            root = loader.load();
            stage.setTitle("Grad");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(true);
            stage.show();

            stage.setOnHiding( event -> {
                Grad noviGrad = gradController.getGrad();
                if (noviGrad != null) {
                    dao.izmijeniGrad(noviGrad);
                    listGradovi.setAll(dao.gradovi());
                }
            } );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionObrisiGrad(ActionEvent actionEvent) {
        Grad grad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (grad == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje grada "+grad.getNaziv());
        alert.setContentText("Da li ste sigurni da želite obrisati grad " +grad.getNaziv()+"?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            dao.obrisiGrad(grad);
            listGradovi.setAll(dao.gradovi());
        }
    }

    // Metoda za potrebe testova, vraća bazu u polazno stanje
    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }

    public void actionIzvjestaj(ActionEvent actionEvent) {
        try {
            new Izvjestaj().showReport(dao.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void actionZapisi(ActionEvent actionEvent) {
        try {
            Geografija geografija = new Geografija(dao.drzave(),dao.gradovi());
            XMLEncoder izlaz = new XMLEncoder(new FileOutputStream("geografija.xml"));
            izlaz.writeObject(geografija);
            izlaz.close();
        } catch(Exception e) {
            System.out.println("Greška: "+e);
        }
    }

    public Geografija actionUcitaj() {
        Geografija geografija = null;
        try {
            XMLDecoder ulaz = new XMLDecoder(new FileInputStream("geografija.xml"));
            geografija = (Geografija) ulaz.readObject();
            ulaz.close();
        } catch(Exception e) {
            System.out.println("Greška: "+e);
        }
        return geografija;
    }
}
