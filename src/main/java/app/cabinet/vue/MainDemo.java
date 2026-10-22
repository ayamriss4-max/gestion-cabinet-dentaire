package app.cabinet.vue;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainDemo extends Application {

    @Override
    public void start(Stage stage) {
        Label message = new Label("Cabinet dentaire - installation OK");
        stage.setScene(new Scene(message, 400, 200));
        stage.setTitle("Cabinet dentaire");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}