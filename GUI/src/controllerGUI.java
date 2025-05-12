import Kontroller.Automat;
import Modell.Hersteller;
import Modell.Kremkuchen;
import Modell.Kuchen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import kuchen.Allergen;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class controllerGUI  implements Initializable {

    @FXML
    private Label benachrichtigung;

    @FXML
    ListView<Hersteller> listHersteller;
    @FXML
    ListView<Kuchen> listKuchen;

    @FXML
    private ComboBox<String> kuchenMenu = new ComboBox();;

    @FXML
    Label info;

    static Automat automat;
    String fileJBP = "jbp.txt";
    String fileJOS = "jos.txt";

    @FXML
    void addKuchen() {

        Task<Void> addkuch = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(900);
                Platform.runLater(() -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("kuchen.fxml"));
                        Parent root = fxmlLoader.load();
                        ControllerKuchen controllerKuch = fxmlLoader.getController();
                        controllerKuch.initData(automat);
                        controllerKuch.setMainController(controllerGUI.this);
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        info.setText("Fehler ! ");
                        System.err.println(e.getMessage());
                    }
                });

                return null;
            }
        };
        new Thread(addkuch).start();
    }
    public void updateHerstellerList(ObservableList<Hersteller> herstellerList) {
        listHersteller.setItems(herstellerList);
        listHersteller.setCellFactory(param -> new ListCell<Hersteller>() {
            @Override
            protected void updateItem(Hersteller hersteller, boolean zustand) {
                super.updateItem(hersteller, zustand);
                if (zustand || hersteller == null) {
                    setText(null);
                } else {
                    int anzahlKuchen = automat.anzahlGelagerterKuchen().getOrDefault(hersteller, 0);
                    setText(hersteller.getName() + "    hat aktuell  ----->  " + anzahlKuchen + " Kuchen");
                }
            }
        });
    }


    @FXML
    void deleteKuchen() {
        Kuchen selectKuchen = listKuchen.getSelectionModel().getSelectedItem();
        if (selectKuchen != null) {
            automat.deleteKuchen(selectKuchen.getFachnummer());
            refreshList();
            System.out.println(automat.getKuchenList());
        } else {
            benachrichtigung.setText(" Select ein Kuchen");
        }
    }

    @FXML
    void inspektKuchen() {
        Kuchen selectKuchen = listKuchen.getSelectionModel().getSelectedItem();
        if (selectKuchen != null) {
            automat.aendern(selectKuchen);
            System.out.println(automat.Auflisten());
            listKuchen.refresh();
            benachrichtigung.setText(selectKuchen.getInspektionsdatum().toString());
        } else {
            benachrichtigung.setText("Select ein Kuchen");
        }

    }
    @FXML
    void saveJBP() {
        automat.saveJBP(fileJBP);
    }

    @FXML
    void saveJOS() {
    automat.saveJOS(fileJOS);
    }

    @FXML
    void loadJBP() {
        try {
            Automat loadedAutomat = automat.ladenJBP(fileJBP);
            if (loadedAutomat != null) {
                this.automat = loadedAutomat;
                refreshNachLoad();
                info.setText("Daten erfolgreich mit JBP geladen.");
            } else {
                info.setText("Fehler beim Load Mit JBP.");
            }
        } catch (Exception e) {
            info.setText("Fehler beim Load: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void loadJOS() {
        try {
            Automat refreshAutomat = automat.ladenJOS(fileJOS);
            if (refreshAutomat != null) {
                this.automat = refreshAutomat;
                refreshNachLoad();
                info.setText("Daten erfolgreich aus JBP geladen.");
            } else {
                info.setText("Fehler beim Laden der Daten aus JBP.");
            }
        } catch (Exception e) {
            info.setText("Fehler beim Load " + e.getMessage());
            e.printStackTrace();
        }
    }

    void refreshNachLoad() {
        try {
            ObservableList<Hersteller> observableHers = FXCollections.observableArrayList(automat.getHerstellerList());
            listHersteller.setItems(observableHers);
            listHersteller.refresh();
            ObservableList<Kuchen> observableKuchen = FXCollections.observableArrayList(automat.getKuchenList());
            listKuchen.setItems(observableKuchen);
            listKuchen.refresh();
        }catch (NullPointerException e) {
            System.err.println("Automat leer.");
        } catch (Exception e) {
            System.err.println("Fehler " + e.getMessage());
        }
    }

    @FXML
    void addHersteller() {
        try {
            FXMLLoader loaderHersteller = new FXMLLoader(getClass().getResource("hersteller.fxml"));
            Parent rootHerst = loaderHersteller.load();

            ControllerHersteller controllerHerst = loaderHersteller.getController();
            controllerHerst.initData(automat);
            controllerHerst.setMainController(this);

            Scene scene = new Scene(rootHerst);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            System.out.println(automat.getHerstellerList());
        } catch (IOException e) {
            info.setText(e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void deleteHersteller() {
        try {
            Hersteller selectHersteller = listHersteller.getSelectionModel().getSelectedItem();
            System.out.println(selectHersteller.getName());
            if (selectHersteller != null) {
                if (automat != null) {
                    automat.deleteHersteller(selectHersteller);
                    refreshList();
                    System.out.println(automat.getHerstellerList());
                    info.setText("Hersteller successful geloescht");
                } else {
                    info.setText("Automat nicht initialisiert oder leer");
                }
            } else {
                info.setText("Select Hersteller");
            }
        } catch (Exception e) {
            info.setText(e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    void refreshList() {
        ObservableList<Kuchen> observableKuchen = FXCollections.observableArrayList(automat.getKuchenList());
        ObservableList<Hersteller> observableHersteller = FXCollections.observableArrayList(automat.getHerstellerList());
        listKuchen.setItems(observableKuchen);
        listHersteller.setItems(observableHersteller);
        listHersteller.refresh();
        listKuchen.refresh();
    }
    @FXML
    void sortieren() {
        System.out.println(kuchenMenu.getSelectionModel().getSelectedItem());
        Object select = kuchenMenu.getSelectionModel().getSelectedItem();
        if (select != null) {
            if(automat.getHerstellerList() != null){
                String selectValue = select.toString();
                System.out.println("gefiltet nach : " + selectValue);
                switch (selectValue) {
                    case "Fach":
                        List<Kuchen> groupByFach = automat.getKuchenList().stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparing(Kuchen::getFachnummer, Comparator.nullsLast(Integer::compareTo)))
                        .collect(Collectors.toList());
                        ObservableList<Kuchen> observableFach = FXCollections.observableArrayList(groupByFach);

                        listKuchen.setItems(observableFach);
                        listKuchen.refresh();
                        break;
                    case "Hersteller":
                        List<Kuchen> groupByHerst = automat.getKuchenList().stream()
                                .filter(Objects::nonNull) // filtert erst null kuchen = position frei
                                .sorted(Comparator.comparing((Kuchen kuchen) -> kuchen.getHersteller() == null ? null : kuchen.getHersteller().getName(), Comparator.nullsLast(String::compareTo)))
                                .collect(Collectors.toList());
                        ObservableList<Kuchen> observableHersteller = FXCollections.observableArrayList(groupByHerst);
                        listKuchen.setItems(observableHersteller);
                        listKuchen.refresh();
                        break;
                    case "Inspektion":
                        List<Kuchen> groupByInsp = automat.getKuchenList().stream()
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(Kuchen::getInspektionsdatum, Comparator.nullsLast(Comparator.naturalOrder())))
                                .collect(Collectors.toList());
                        ObservableList<Kuchen> observableInspektion = FXCollections.observableArrayList(groupByInsp);
                        listKuchen.setItems(observableInspektion);
                        listKuchen.refresh();

                        break;
                    case "Haltbarkeit":
                        List<Kuchen> groupByHaltb = automat.getKuchenList().stream()
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(Kuchen::getHaltbarkeit, Comparator.nullsLast(Comparator.naturalOrder())))
                                .collect(Collectors.toList());
                        ObservableList<Kuchen> observableHaltbarkeit = FXCollections.observableArrayList(groupByHaltb);
                        listKuchen.setItems(observableHaltbarkeit);
                        listKuchen.refresh();

                        break;
                    default:
                        System.out.println("Option ungueltig");
                        break;
                }
            }else{
                info.setText("kein Kuchen");
            }
        } else {
            System.out.println("Select eine Option");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automat = new Automat(5);
        ObservableList<Kuchen> observableKuch = FXCollections.observableArrayList(automat.getKuchenList());
        ObservableList<Hersteller> observableHers = FXCollections.observableArrayList(automat.getHerstellerList());
        ObservableList<String> wahl = FXCollections.observableArrayList("Fach", "Hersteller", "Inspektion", "Haltbarkeit");
        kuchenMenu.setItems(wahl);
        kuchenMenu.setOnAction(event -> sortieren());

        listKuchen.setItems(observableKuch);
        listHersteller.setItems(observableHers);
        info.setText("Wilkommen !!!");
        System.out.println(kuchenMenu.getItems());
    }

}