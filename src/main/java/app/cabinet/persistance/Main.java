package app.cabinet.persistance;

import app.cabinet.service.Donnee;
import app.cabinet.service.DonneesDemo;
import app.cabinet.ui.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Donnee.chargerTout();      // lit les fichiers CSV (crée admin / admin123 au premier lancement)
        DonneesDemo.creerSiVide(); // dentistes, actes, créneaux et patient1 de démonstration
        Donnee.chargerRdvs();      // relit les rendez-vous déjà réservés

        stage.setTitle("Cabinet dentaire");
        new LoginView(stage).afficher();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}