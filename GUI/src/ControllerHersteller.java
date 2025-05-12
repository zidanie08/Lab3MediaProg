/*public class ControllerHersteller {
}*/
import Kontroller.Automat;
import Modell.Hersteller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class ControllerHersteller {

    @FXML
    TextField name;

    @FXML
    Label info;

    Automat automat;

    private controllerGUI mainController;
    public void setMainController(controllerGUI mainController) {
        this.mainController = mainController;
    }

    public void initData(Automat automat) {
        this.automat = automat;
    }
    @FXML
    void valider() {
        try{
            this.automat.addHersteller(name.getText());
            mainController.updateHerstellerList(FXCollections.observableArrayList(automat.getHerstellerList()));
            info.setText("Hersteller wurde Hinzugefuegt");
        }catch(Exception e){
            info.setText("Fehler: "+ e.getMessage());
            System.err.println(e.getMessage());
        }
    }

}
