import Kontroller.Automat;
import Modell.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import kuchen.Allergen;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ControllerKuchen implements Initializable {

    @FXML
    private TextField haltbarkeit;


    @FXML
    private ComboBox<String> sorte = new ComboBox<>();

    @FXML
    private ComboBox<Hersteller> herstellerBox = new ComboBox<>();


    @FXML
    private TextField preis;

    @FXML
    private TextField naehrwert;

    @FXML
    private TextField name2;

    @FXML
    private ComboBox<Allergen> allergene = new ComboBox<>();


    @FXML
    private TextField name1;

    @FXML
    private Label info;
    private Automat automat;
    private controllerGUI mainController;
    public void setMainController(controllerGUI mainController) {
        this.mainController = mainController;
    }

    public void initData(Automat automat) {
        this.automat = automat;

    }

    @FXML
    void valider() {
        // Pruefen erst oder alle Felder ausgefuellt sind
        if (sorte.getValue() == null ||
                herstellerBox.getValue() == null ||
                allergene.getValue() == null ||
                naehrwert.getText().isEmpty() ||
                haltbarkeit.getText().isEmpty() ||
                preis.getText().isEmpty() ||
                (sorte.getValue().equals("Kremkuchen") && name1.getText().isEmpty()) ||
                (sorte.getValue().equals("Obstkuchen") && name2.getText().isEmpty()) ||
                (sorte.getValue().equals("Obsttorte") && (name1.getText().isEmpty() || name2.getText().isEmpty()))) {
            info.setText("Alle Felder Ausfuellen !");
            return;
        }
        String selectedSorte = sorte.getValue();
        Hersteller selectedHersteller = herstellerBox.getValue();
        Collection<Allergen> selectedAllergene = Collections.singleton(allergene.getSelectionModel().getSelectedItem());
        int selectedNaehrwert = Integer.parseInt(naehrwert.getText());
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expirationTime = LocalDate.parse(haltbarkeit.getText(), format);
        Duration selectedHaltbarkeit = Duration.between(localDate.atStartOfDay(), expirationTime.atStartOfDay());
        BigDecimal selectedPreis = new BigDecimal(preis.getText());
        LocalDate expirationDate = null;
        try {
            format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            expirationDate = LocalDate.parse(haltbarkeit.getText(), format);

            // Pruefen ob das Datum zulaessig sein kann ( alt oder falsche Format)
            if (expirationDate.isBefore(LocalDate.now())) {
                info.setText("Darum kann nicht in der vErgangenheit liegen");
                return;
            }
        } catch (DateTimeParseException e) {
            info.setText("Format ungueltig");
            return;
        }

        switch (selectedSorte) {
            case "Kremkuchen":
                String kremsorte = name1.getText();
                Kremkuchen kremkuchen = new Kremkuchen(selectedHersteller, selectedAllergene, selectedNaehrwert, selectedHaltbarkeit, selectedPreis, kremsorte);
                automat.addKuchen(kremkuchen);
                break;
            case "Obstkuchen":
                String obstsorte = name2.getText();
                Obstkuchen obstkuchen = new Obstkuchen(selectedHersteller, selectedAllergene, selectedNaehrwert, selectedHaltbarkeit, selectedPreis, obstsorte);
                automat.addKuchen(obstkuchen);
                break;
            case "Obsttorte":
                String obstsorteTorte = name2.getText();
                String kremsorteTorte = name1.getText();
                Obsttorte obsttorte = new Obsttorte(selectedHersteller, selectedAllergene, selectedNaehrwert, selectedHaltbarkeit, obstsorteTorte, kremsorteTorte, selectedPreis);
                automat.addKuchen(obsttorte);
                break;
        }
    mainController.refreshList();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> kuchenSorte = FXCollections.observableArrayList("Kremkuchen", "Obstkuchen", "Obsttorte");
        ObservableList<Allergen> allergenSorte = FXCollections.observableArrayList(Allergen.Gluten, Allergen.Erdnuss, Allergen.Haselnuss, Allergen.Sesamsamen);
        sorte.setItems(kuchenSorte);
        allergene.setItems(allergenSorte);
        herstellerBox.setDisable(true);
        info.setText("refresh Hersteller".toUpperCase());
        sorte.setOnAction(event -> {
            String selectedSorte = sorte.getValue();
            switch (selectedSorte) {
                case "Kremkuchen":
                    name1.setDisable(false);
                    name2.setDisable(true);
                    break;
                case "Obstkuchen":
                    name1.setDisable(true);
                    name2.setDisable(false);
                    break;
                case "Obsttorte":
                    name2.setDisable(false);
                    name1.setDisable(false);
                    break;
            }
        });
    }

    @FXML
    void refreshHersteller() {
        if(automat.getHerstellerList()== null || automat.getHerstellerList().isEmpty()){
            herstellerBox.setDisable(true);
            info.setText("kein Hersteller in der Liste");
        }else{
            herstellerBox.setDisable(false);
            ObservableList<Hersteller> herstellerWahl = FXCollections.observableArrayList(automat.getHerstellerList());
            herstellerBox.setItems(herstellerWahl);
            info.setText("");
        }
    }

}
