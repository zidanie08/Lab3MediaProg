import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;


public class
MainGUI extends Application {
     @Override
     public void start(Stage primaryStage) throws Exception{
         Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sample.fxml")));

         primaryStage.setTitle("HTW BÄCKEREI");
         Scene scene = new Scene(root, 900, 700);
         primaryStage.setScene(scene);
         primaryStage.setResizable(true);
         primaryStage.setMinWidth(600);
         primaryStage.setMinHeight(500);

         primaryStage.show();
     }
    public static void main(String[] args) {
        launch(args);
    }
}
