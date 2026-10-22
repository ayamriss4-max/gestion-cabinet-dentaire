import service.Donnee;
import service.DonneesDemo;
import ui.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Donnee.chargerTout();          // lit les fichiers CSV
        DonneesDemo.creerSiVide();     // dentistes, actes, créneaux et patient1 de démonstration
        Donnee.chargerRdvs();          // relit les rendez-vous déjà réservés
        stage.setTitle("Cabinet dentaire");
        new LoginView(stage).afficher();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}