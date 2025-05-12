import Kontroller.Automat;
import Modell.Hersteller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class ControllerTest {

    @Mock
    private Automat automatMock;

    @Mock
    private ListView<Hersteller> listHersMock;

    private controllerGUI controller;

    @BeforeAll
    public static void setUpJavaFX() throws InterruptedException { // Quellen: per Screenshot
        final CountDownLatch count = new CountDownLatch(1);
        new Thread(() -> Application.launch(App.class)).start();
        count.await(5, TimeUnit.SECONDS);
    }
    public static class App extends Application {
        @Override
        public void start(Stage primaryStage) {
            Platform.runLater(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new controllerGUI();
        controller.automat = automatMock;
        controller.listHersteller = listHersMock;
    }

    @AfterAll
    public static void stoppJavaFx() {
        Platform.exit();
    }


    @Test
    void testUpdateHerstellerList() {
        ObservableList<Hersteller> herstellerList = FXCollections.observableArrayList(new Hersteller("hersteller"));
        when(automatMock.anzahlGelagerterKuchen()).thenReturn(Collections.singletonMap(new Hersteller("hersteller"), 1));

        controller.updateHerstellerList(herstellerList);

        verify(listHersMock, times(1)).setItems(herstellerList);
    }

}
