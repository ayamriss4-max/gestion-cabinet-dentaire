package app.cabinet.ui;

import app.cabinet.modele.Administrateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AdminView {

    private Stage stage;
    private Administrateur admin;

    public AdminView(Stage stage, Administrateur admin) {
        this.stage = stage;
        this.admin = admin;
    }

    public void afficher() {
        Label titre = new Label("Espace administrateur : " + admin.getLogin());
        titre.getStyleClass().add("titre");

        Button btnDeconnexion = new Button("Déconnexion");
        btnDeconnexion.setOnAction(e -> new LoginView(stage).afficher());

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        HBox barre = new HBox(10, titre, espace, btnDeconnexion);
        barre.setPadding(new Insets(10));
        barre.setAlignment(Pos.CENTER_LEFT);

        TabPane onglets = new TabPane();
        onglets.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        onglets.getTabs().add(new Tab("Patients", new PatientsPane()));
        onglets.getTabs().add(new Tab("Dentistes", emplacement("Dentistes")));
        onglets.getTabs().add(new Tab("Actes", emplacement("Actes")));
        onglets.getTabs().add(new Tab("Créneaux", emplacement("Créneaux")));
        onglets.getTabs().add(new Tab("Rendez-vous", emplacement("Rendez-vous")));

        BorderPane racine = new BorderPane();
        racine.setTop(barre);
        racine.setCenter(onglets);

        stage.setScene(Style.creerScene(racine, 1100, 680));
    }

    // Emplacement provisoire, remplacé par le panneau de la coéquipière responsable
    private Label emplacement(String nom) {
        Label label = new Label("Onglet " + nom + " : à intégrer par la personne responsable.");
        label.setPadding(new Insets(20));
        return label;
    }
}