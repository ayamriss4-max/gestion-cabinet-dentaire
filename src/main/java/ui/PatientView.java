package ui;

import modele.Acte;
import modele.Creneau;
import modele.Dentiste;
import modele.Patient;
import modele.RendezVous;
import service.Donnee;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientView {

    // Code couleur du planning
    private static final String VERT = "-fx-background-color: #2ecc71; -fx-text-fill: white;";
    private static final String ROUGE = "-fx-background-color: #e74c3c; -fx-text-fill: white;";
    private static final String ORANGE = "-fx-background-color: #f39c12; -fx-text-fill: white;";
    private static final String BLEU = "-fx-background-color: #2980b9; -fx-text-fill: white;";

    private Stage stage;
    private Patient patient;
    private ComboBox<Dentiste> comboDentiste = new ComboBox<>();
    private ComboBox<Acte> comboActe = new ComboBox<>();
    private GridPane grille = new GridPane();
    private Label message = new Label();
    private Creneau creneauChoisi = null;
    private HistoriquePane historique;

    public PatientView(Stage stage, Patient patient) {
        this.stage = stage;
        this.patient = patient;
    }

    public void afficher() {
        // Barre du haut
        Label titre = new Label("Espace patient : " + patient.getPrenom() + " " + patient.getNom());
        titre.getStyleClass().add("titre");

        Button btnDeconnexion = new Button("Déconnexion");
        btnDeconnexion.setOnAction(e -> new LoginView(stage).afficher());

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        HBox barre = new HBox(10, titre, espace, btnDeconnexion);
        barre.setPadding(new Insets(10));
        barre.setAlignment(Pos.CENTER_LEFT);

        // Choix du dentiste et de l'acte
        comboDentiste.setItems(FXCollections.observableArrayList(Donnee.dentistes));
        comboDentiste.setPromptText("Choisir un dentiste");
        comboActe.setItems(FXCollections.observableArrayList(Donnee.actes));
        comboActe.setPromptText("Choisir un acte");

        comboDentiste.setOnAction(e -> {
            creneauChoisi = null;
            message.setText("");
            dessinerPlanning();
        });
        comboActe.setOnAction(e -> {
            creneauChoisi = null;
            message.setText("");
            dessinerPlanning();
        });

        HBox choix = new HBox(10, new Label("Dentiste :"), comboDentiste, new Label("Acte :"), comboActe);
        choix.setAlignment(Pos.CENTER_LEFT);

        Label legende = new Label("Légende : vert = libre, rouge = occupé, orange = urgence, bleu = votre choix");

        grille.setHgap(6);
        grille.setVgap(6);
        grille.setPadding(new Insets(10));
        ScrollPane defilement = new ScrollPane(grille);
        defilement.setPrefHeight(350);

        Button btnReserver = new Button("Réserver ce créneau");
        btnReserver.setOnAction(e -> reserver());

        VBox reservation = new VBox(10, choix, legende, defilement, message, btnReserver);
        reservation.setPadding(new Insets(10));

        // Onglets : réservation et historique (tâche 3.7)
        historique = new HistoriquePane(patient);
        Tab tabReservation = new Tab("Réserver un rendez-vous", reservation);
        Tab tabHistorique = new Tab("Mes rendez-vous", historique);
        tabHistorique.setOnSelectionChanged(e -> {
            if (tabHistorique.isSelected()) {
                historique.rafraichir();
            }
        });

        TabPane onglets = new TabPane(tabReservation, tabHistorique);
        onglets.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane racine = new BorderPane();
        racine.setTop(barre);
        racine.setCenter(onglets);

        stage.setScene(Style.creerScene(racine, 1000, 650));
    }

    // ---------- Règles métier ----------

    // Un créneau est libre si aucun rendez-vous non annulé ne l'occupe
    private boolean estLibre(Creneau c) {
        return Donnee.rdvDuCreneau(c) == null;
    }

    // Vrai si la durée de l'acte tient dans le créneau
    private boolean acteTient(Creneau c, Acte acte) {
        long minutes = Duration.between(c.getHeureDebut(), c.getHeureFin()).toMinutes();
        return acte.getDuree() <= minutes;
    }

    // ---------- Dessin du planning ----------

    // Dessine la grille : jours en colonnes, heures en lignes
    private void dessinerPlanning() {
        grille.getChildren().clear();
        Dentiste dentiste = comboDentiste.getValue();
        if (dentiste == null) {
            return;
        }
        LocalDate debut = LocalDate.now();

        // En-têtes des 7 jours
        for (int j = 0; j < 7; j++) {
            LocalDate jour = debut.plusDays(j);
            String nomJour = jour.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRENCH);
            Label etiquette = new Label(nomJour + " " + jour.getDayOfMonth() + "/" + jour.getMonthValue());
            etiquette.setStyle("-fx-font-weight: bold;");
            grille.add(etiquette, j + 1, 0);
        }

        // Liste des heures de début de ce dentiste, triées
        List<LocalTime> heures = new ArrayList<>();
        for (Creneau c : Donnee.creneaux) {
            if (c.getDentiste().getIdDentiste() == dentiste.getIdDentiste()
                    && !heures.contains(c.getHeureDebut())) {
                heures.add(c.getHeureDebut());
            }
        }
        Collections.sort(heures);

        // Une ligne par heure, un bouton par créneau existant
        for (int i = 0; i < heures.size(); i++) {
            grille.add(new Label(heures.get(i).toString()), 0, i + 1);
            for (int j = 0; j < 7; j++) {
                Creneau c = chercherCreneau(dentiste, debut.plusDays(j), heures.get(i));
                if (c != null) {
                    grille.add(creerBouton(c), j + 1, i + 1);
                }
            }
        }
    }

    private Creneau chercherCreneau(Dentiste dentiste, LocalDate jour, LocalTime heure) {
        // On ne propose pas les heures déjà passées aujourd'hui
        if (jour.equals(LocalDate.now()) && heure.isBefore(LocalTime.now())) {
            return null;
        }
        for (Creneau c : Donnee.creneaux) {
            if (c.getDentiste().getIdDentiste() == dentiste.getIdDentiste()
                    && c.getDate().equals(jour) && c.getHeureDebut().equals(heure)) {
                return c;
            }
        }
        return null;
    }

    private Button creerBouton(Creneau c) {
        Button bouton = new Button(c.getHeureDebut() + "-" + c.getHeureFin());
        bouton.setMinWidth(105);
        bouton.setStyle(couleurDe(c));
        bouton.setOnAction(e -> choisir(c));
        return bouton;
    }

    private String couleurDe(Creneau c) {
        if (!estLibre(c)) {
            return ROUGE;
        }
        if (c == creneauChoisi) {
            return BLEU;
        }
        if ("urgence".equals(c.getType())) {
            return ORANGE;
        }
        return VERT;
    }

    // ---------- Actions ----------

    // Clic sur un créneau : vérifications puis passage en bleu
    private void choisir(Creneau c) {
        if (!estLibre(c)) {
            message.setText("Ce créneau est déjà occupé.");
            return;
        }
        Acte acte = comboActe.getValue();
        if (acte == null) {
            message.setText("Choisissez d'abord un acte.");
            return;
        }
        if (!acteTient(c, acte)) {
            message.setText("L'acte dure " + acte.getDuree() + " min : il ne tient pas dans ce créneau.");
            return;
        }
        creneauChoisi = c;
        message.setText("Créneau choisi : " + c.getDate() + " de " + c.getHeureDebut() + " à " + c.getHeureFin());
        dessinerPlanning();
    }

    // Confirmation : création du rendez-vous en attente
    private void reserver() {
        Acte acte = comboActe.getValue();
        if (creneauChoisi == null || acte == null) {
            Alerte.erreur("Choisissez un dentiste, un acte et un créneau libre.");
            return;
        }
        // Revérification juste avant d'enregistrer (empêche la double réservation)
        if (!estLibre(creneauChoisi)) {
            Alerte.erreur("Ce créneau vient d'être pris. Choisissez-en un autre.");
            creneauChoisi = null;
            dessinerPlanning();
            return;
        }
        if (!acteTient(creneauChoisi, acte)) {
            Alerte.erreur("La durée de l'acte ne tient pas dans ce créneau.");
            return;
        }

        Creneau c = creneauChoisi;
        RendezVous rdv = new RendezVous(Donnee.prochainIdRdv(), LocalDate.now(), c.getDate(),
                RendezVous.ATTENTE, patient, acte, c);
        Donnee.rdvs.add(rdv);
        Donnee.sauvegarderRdvs();

        String nl = System.lineSeparator();
        Alerte.info("Rendez-vous enregistré (état : en attente)." + nl + nl
                + "Patient : " + patient.getNom() + " " + patient.getPrenom() + nl
                + "Dentiste : " + c.getDentiste() + nl
                + "Acte : " + acte.getLibelle() + nl
                + "Date : " + c.getDate() + " à " + c.getHeureDebut() + nl
                + "Durée prévue : " + acte.getDuree() + " min" + nl
                + "Tarif : " + acte.getTarif() + " DH");

        creneauChoisi = null;
        message.setText("");
        dessinerPlanning(); // le créneau réservé passe au rouge
    }
}