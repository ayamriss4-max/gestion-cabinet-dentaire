package app.cabinet.ui;

import app.cabinet.modele.Patient;
import app.cabinet.service.Donnee;
import app.cabinet.util.MotDePasse;
import app.cabinet.util.Validation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InscriptionView {

    private Stage stage;
    private TextField champLogin = new TextField();
    private PasswordField champPwd = new PasswordField();
    private PasswordField champPwd2 = new PasswordField();
    private TextField champCin = new TextField();
    private TextField champNom = new TextField();
    private TextField champPrenom = new TextField();
    private TextField champAdresse = new TextField();
    private TextField champTelephone = new TextField();
    private DatePicker champDate = new DatePicker();
    private Label message = new Label();

    public InscriptionView(Stage stage) {
        this.stage = stage;
    }

    public void afficher() {
        Label titre = new Label("Création d'un compte patient");
        titre.getStyleClass().add("titre");
        message.getStyleClass().add("erreur");
        message.setWrapText(true);

        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(8);
        grille.addRow(0, new Label("Login"), champLogin);
        grille.addRow(1, new Label("Mot de passe"), champPwd);
        grille.addRow(2, new Label("Confirmer le mot de passe"), champPwd2);
        grille.addRow(3, new Label("CIN"), champCin);
        grille.addRow(4, new Label("Nom"), champNom);
        grille.addRow(5, new Label("Prénom"), champPrenom);
        grille.addRow(6, new Label("Adresse"), champAdresse);
        grille.addRow(7, new Label("Téléphone"), champTelephone);
        grille.addRow(8, new Label("Date de naissance"), champDate);

        Button btnCreer = new Button("Créer le compte");
        Button btnRetour = new Button("Retour");
        btnCreer.setOnAction(e -> creer());
        btnRetour.setOnAction(e -> new LoginView(stage).afficher());

        VBox boite = new VBox(12, titre, grille, message, new HBox(10, btnCreer, btnRetour));
        boite.setAlignment(Pos.CENTER);
        boite.setPadding(new Insets(20));
        boite.setMaxWidth(450);

        stage.setScene(Style.creerScene(new StackPane(boite), 600, 620));
    }

    private void creer() {
        String erreur = verifier();
        if (erreur != null) {
            message.setText(erreur);
            return;
        }
        Patient p = new Patient(champLogin.getText().trim(), MotDePasse.hacher(champPwd.getText()),
                champCin.getText().trim().toUpperCase(), champNom.getText().trim(),
                champPrenom.getText().trim(), champAdresse.getText().trim(),
                champTelephone.getText().trim(), champDate.getValue());
        Donnee.utilisateurs.add(p);
        Donnee.sauvegarderUtilisateurs();
        Alerte.info("Compte créé. Vous pouvez maintenant vous connecter.");
        new LoginView(stage).afficher();
    }

    // Renvoie un message d'erreur, ou null si tout est correct
    private String verifier() {
        String login = champLogin.getText().trim();
        if (login.length() < 3 || !Validation.sansPointVirgule(login)) {
            return "Login : 3 caractères minimum, sans point-virgule.";
        }
        if (Donnee.chercherParLogin(login) != null) {
            return "Ce login est déjà utilisé.";
        }
        if (champPwd.getText().length() < 6) {
            return "Mot de passe : 6 caractères minimum.";
        }
        if (!champPwd.getText().equals(champPwd2.getText())) {
            return "Les deux mots de passe sont différents.";
        }
        String cin = champCin.getText().trim();
        if (!Validation.cinValide(cin)) {
            return "CIN invalide (exemple : AB123456).";
        }
        if (Donnee.chercherPatientParCin(cin) != null) {
            return "Un compte existe déjà avec ce CIN.";
        }
        if (!Validation.textePresent(champNom.getText()) || !Validation.textePresent(champPrenom.getText())) {
            return "Le nom et le prénom sont obligatoires.";
        }
        if (!Validation.telephoneValide(champTelephone.getText().trim())) {
            return "Téléphone invalide (exemple : 0612345678).";
        }
        if (!Validation.dateNaissanceValide(champDate.getValue())) {
            return "La date de naissance est obligatoire et doit être dans le passé.";
        }
        return null;
    }
}