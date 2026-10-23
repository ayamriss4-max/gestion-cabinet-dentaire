package app.cabinet.persistance;

import app.cabinet.ui.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Lance l'écran de connexion
        new LoginView(primaryStage).afficher();
    }

    public static void main(String[] args) {
        launch(args);
    }
}