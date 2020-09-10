package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class PretragaController {
    public TextField pretragaField;
    public ListView<String> putanjeListView;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public PretragaController(String imagePath) {
        this.imagePath=imagePath;
    }

    @FXML
    public void initialize() {
        putanjeListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount()==2) {
                    setImagePath(putanjeListView.getSelectionModel().getSelectedItem());
                    Stage stage = (Stage) pretragaField.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    public void pretragaAction(ActionEvent actionEvent) {
        if(!pretragaField.getText().trim().equals("")) {
            new Thread(() -> {
                izvrsiPretragu("C:\\",pretragaField.getText());
            }).start();
        }
    }

    public void izvrsiPretragu(String path, String search) {
        for(String clan: Objects.requireNonNull(new File(path).list())) {
            if(path.equals("C:\\")) {
                //System.out.println(path+clan);
                if(!(new File(path+clan).isFile())) {
                    //System.out.println(path+clan);
                    izvrsiPretragu(path+clan,search);
                }
                else {
                    if((path+clan).contains(search)) putanjeListView.getItems().add(path+clan);
                }
            }
            else {
                //System.out.println(path+"\\"+clan);
                if(!(new File(path+"\\"+clan).isFile())) {
                    //System.out.println(path+"\\"+clan);
                    izvrsiPretragu(path+"\\"+clan,search);
                }
                else {
                    if((path+"\\"+clan).contains(search)) putanjeListView.getItems().add(path+"\\"+clan);
                }
            }
        }
    }
}
