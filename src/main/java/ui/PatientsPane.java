package ui;

import modele.Patient;
import service.Donnee;
import util.MotDePasse;
import util.Validation;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PatientsPane extends VBox {

    private TableView<Patient> table = new TableView<>();
    private TextField champCin = new TextField();
    private TextField champNom = new TextField();
    private TextField champPrenom = new TextField();
    private TextField champAdresse = new TextField();
    private TextField champTelephone = new TextField();
    private DatePicker champDate = new DatePicker();
    private TextField champLogin = new TextField();
    private PasswordField champPwd = new PasswordField();

    public PatientsPane() {
        setSpacing(10);
        setPadding(new Insets(10));
        champPwd.setPromptText("Vide = inchangé");
        creerTable();
        getChildren().addAll(table, creerFormulaire(), creerBoutons());
        rafraichir();
    }

    // ---------- Construction de l'interface ----------

    private void ajouterColonne(String titre, String propriete) {
        TableColumn<Patient, String> colonne = new TableColumn<>(titre);
        colonne.setCellValueFactory(new PropertyValueFactory<>(propriete));
        table.getColumns().add(colonne);
    }

    private void creerTable() {
        ajouterColonne("CIN", "cin");
        ajouterColonne("Nom", "nom");
        ajouterColonne("Prénom", "prenom");
        ajouterColonne("Adresse", "adresse");
        ajouterColonne("Téléphone", "telephone");
        TableColumn<Patient, LocalDate> colDate = new TableColumn<>("Date de naissance");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        table.getColumns().add(colDate);
        ajouterColonne("Login", "login");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(300);

        // Un clic sur une ligne remplit le formulaire
        table.getSelectionModel().selectedItemProperty().addListener((obs, ancien, p) -> {
            if (p != null) {
                remplirFormulaire(p);
            }
        });
    }

    private GridPane creerFormulaire() {
        GridPane grille = new GridPane();
        grille.setHgap(10);
        grille.setVgap(8);
        grille.addRow(0, new Label("CIN"), champCin, new Label("Nom"), champNom,
                new Label("Prénom"), champPrenom);
        grille.addRow(1, new Label("Adresse"), champAdresse, new Label("Téléphone"), champTelephone,
                new Label("Date de naissance"), champDate);
        grille.addRow(2, new Label("Login"), champLogin, new Label("Mot de passe"), champPwd);
        return grille;
    }

    private HBox creerBoutons() {
        Button btnAjouter = new Button("Ajouter");
        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        Button btnVider = new Button("Vider le formulaire");

        btnAjouter.setOnAction(e -> ajouter());
        btnModifier.setOnAction(e -> modifier());
        btnSupprimer.setOnAction(e -> supprimer());
        btnVider.setOnAction(e -> viderFormulaire());

        return new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnVider);
    }

    // ---------- Actions ----------

    private void ajouter() {
        String erreur = verifierSaisie(null);
        if (erreur != null) {
            Alerte.erreur(erreur);
            return;
        }
        Patient p = new Patient(champLogin.getText().trim(), MotDePasse.hacher(champPwd.getText()),
                champCin.getText().trim().toUpperCase(), champNom.getText().trim(),
                champPrenom.getText().trim(), champAdresse.getText().trim(),
                champTelephone.getText().trim(), champDate.getValue());
        Donnee.utilisateurs.add(p);
        Donnee.sauvegarderUtilisateurs();
        rafraichir();
        viderFormulaire();
        Alerte.info("Patient ajouté.");
    }

    private void modifier() {
        Patient p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alerte.erreur("Sélectionnez un patient dans le tableau.");
            return;
        }
        String erreur = verifierSaisie(p);
        if (erreur != null) {
            Alerte.erreur(erreur);
            return;
        }
        p.setCin(champCin.getText().trim().toUpperCase());
        p.setNom(champNom.getText().trim());
        p.setPrenom(champPrenom.getText().trim());
        p.setAdresse(champAdresse.getText().trim());
        p.setTelephone(champTelephone.getText().trim());
        p.setDateNaissance(champDate.getValue());
        if (!champPwd.getText().isEmpty()) {
            p.changerMotDePasse(champPwd.getText());
        }
        Donnee.sauvegarderUtilisateurs();
        rafraichir();
        viderFormulaire();
        Alerte.info("Patient modifié.");
    }

    private void supprimer() {
        Patient p = table.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alerte.erreur("Sélectionnez un patient dans le tableau.");
            return;
        }
        if (Donnee.patientADesRdv(p)) {
            Alerte.erreur("Ce patient a des rendez-vous : suppression impossible.");
            return;
        }
        if (!Alerte.confirmer("Supprimer le patient " + p.getNom() + " " + p.getPrenom() + " ?")) {
            return;
        }
        Donnee.utilisateurs.remove(p);
        Donnee.sauvegarderUtilisateurs();
        rafraichir();
        viderFormulaire();
    }

    // ---------- Aides ----------

    // Renvoie un message d'erreur, ou null si la saisie est correcte.
    // courant = null pour un ajout, le patient sélectionné pour une modification.
    private String verifierSaisie(Patient courant) {
        String cin = champCin.getText().trim();
        if (!Validation.cinValide(cin)) {
            return "CIN invalide (exemple : AB123456, 1 ou 2 lettres puis 5 ou 6 chiffres).";
        }
        Patient autre = Donnee.chercherPatientParCin(cin);
        if (autre != null && autre != courant) {
            return "Ce CIN existe déjà.";
        }
        if (!Validation.textePresent(champNom.getText()) || !Validation.textePresent(champPrenom.getText())) {
            return "Le nom et le prénom sont obligatoires.";
        }
        if (!Validation.telephoneValide(champTelephone.getText().trim())) {
            return "Téléphone invalide (exemple : 0612345678 ou +212612345678).";
        }
        if (!Validation.dateNaissanceValide(champDate.getValue())) {
            return "La date de naissance est obligatoire et doit être dans le passé.";
        }
        if (courant == null) {
            String login = champLogin.getText().trim();
            if (login.length() < 3 || !Validation.sansPointVirgule(login)) {
                return "Login : 3 caractères minimum, sans point-virgule.";
            }
            if (Donnee.chercherParLogin(login) != null) {
                return "Ce login existe déjà.";
            }
            if (champPwd.getText().length() < 6) {
                return "Mot de passe : 6 caractères minimum.";
            }
        } else if (!champPwd.getText().isEmpty() && champPwd.getText().length() < 6) {
            return "Mot de passe : 6 caractères minimum.";
        }
        return null;
    }

    private void remplirFormulaire(Patient p) {
        champCin.setText(p.getCin());
        champNom.setText(p.getNom());
        champPrenom.setText(p.getPrenom());
        champAdresse.setText(p.getAdresse());
        champTelephone.setText(p.getTelephone());
        champDate.setValue(p.getDateNaissance());
        champLogin.setText(p.getLogin());
        champLogin.setDisable(true); // le login ne change pas
        champPwd.clear();
    }

    private void viderFormulaire() {
        table.getSelectionModel().clearSelection();
        champCin.clear();
        champNom.clear();
        champPrenom.clear();
        champAdresse.clear();
        champTelephone.clear();
        champDate.setValue(null);
        champLogin.clear();
        champLogin.setDisable(false);
        champPwd.clear();
    }

    private void rafraichir() {
        table.setItems(FXCollections.observableArrayList(Donnee.getPatients()));
    }
}