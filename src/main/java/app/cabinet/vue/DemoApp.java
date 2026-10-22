package app.cabinet.vue;

import app.cabinet.modele.*;
import app.cabinet.persistance.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoApp extends Application {

    private final Dentiste dentiste = new Dentiste(1, "Alaoui", "Karim", "Généraliste");
    private final Patient patient = new Patient(1, "Bennani", "Sara");
    private final Acte acte = new Acte(1, "Détartrage", 30, 300.0);

    private final List<Creneau> creneaux = new ArrayList<>();
    private final PlanningView planning = new PlanningView();
    private int prochainRdv = 100;

    @Override
    public void start(Stage stage) {
        LocalDate lundi = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int id = 100;
        for (int jour = 0; jour < 5; jour++) {
            LocalDate d = lundi.plusDays(jour);
            creneaux.add(new Creneau(id++, d + " 09:00", 60, Creneau.NORMAL, dentiste));
            creneaux.add(new Creneau(id++, d + " 10:00", 30, Creneau.URGENCE, dentiste));
            creneaux.add(new Creneau(id++, d + " 14:00", 60, Creneau.NORMAL, dentiste));
        }
        planning.setCreneaux(creneaux);

        Button btnReserver = new Button("Réserver le créneau sélectionné");
        btnReserver.setOnAction(e -> reserver());
        HBox barre = new HBox(10, btnReserver);
        barre.setPadding(new Insets(10));
        BorderPane pagePlanning = new BorderPane(planning);
        pagePlanning.setBottom(barre);

        TabPane onglets = new TabPane(
                new Tab("Planning", pagePlanning),
                new Tab("Rendez-vous", new RdvView()),
                new Tab("Statistiques", new StatsView()));
        onglets.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        stage.setScene(new Scene(onglets, 1000, 650));
        stage.setTitle("Démo - Cabinet dentaire");
        stage.show();
    }

    private void reserver() {
        Creneau c = planning.selectionProperty().get();
        if (c == null) {
            message("Sélectionne d'abord un créneau.");
            return;
        }
        if (c.estPasse()) {
            message("Impossible de réserver un créneau passé.");
            return;
        }
        if (!c.estLibre()) {
            message("Ce créneau est déjà réservé.");
            return;
        }
        if (!c.contient(acte)) {
            message("L'acte ne tient pas dans ce créneau.");
            return;
        }

        CreneauReserve cr = new CreneauReserve(c);
        creneaux.set(creneaux.indexOf(c), cr);
        CabinetDao.getInstance().ajouterRdv(
                new RendezVous(prochainRdv++, new Date(), patient, acte, cr));
        planning.setCreneaux(creneaux);
    }

    private void message(String texte) {
        new Alert(Alert.AlertType.INFORMATION, texte).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 