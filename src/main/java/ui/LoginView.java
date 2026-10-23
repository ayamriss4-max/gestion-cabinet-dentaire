package ui;

import modele.Administrateur;
import modele.Patient;
import modele.Utilisateur;
import service.Donnee;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void afficher() {
        Label titre = new Label("Cabinet dentaire");
        titre.getStyleClass().add("titre");

        TextField champLogin = new TextField();
        champLogin.setPromptText("Login");

        PasswordField champPwd = new PasswordField();
        champPwd.setPromptText("Mot de passe");

        Label erreur = new Label();
        erreur.getStyleClass().add("erreur");

        Button btnConnexion = new Button("Se connecter");
        btnConnexion.setDefaultButton(true); // touche Entrée
        Button btnInscription = new Button("Créer un compte patient");

        btnConnexion.setOnAction(e -> {
            String login = champLogin.getText().trim();
            String pwd = champPwd.getText();
            if (login.isEmpty() || pwd.isEmpty()) {
                erreur.setText("Veuillez saisir votre login et votre mot de passe.");
                return;
            }
            Utilisateur u = Donnee.connecter(login, pwd);
            if (u == null) {
                erreur.setText("Login ou mot de passe incorrect.");
                champPwd.clear();
            } else if (u instanceof Administrateur) {
                new AdminView(stage, (Administrateur) u).afficher();
            } else {
                new PatientView(stage, (Patient) u).afficher();
            }
        });

        btnInscription.setOnAction(e -> new InscriptionView(stage).afficher());

        VBox boite = new VBox(12, titre, champLogin, champPwd, btnConnexion, erreur, btnInscription);
        boite.setAlignment(Pos.CENTER);
        boite.setPadding(new Insets(30));
        boite.setMaxWidth(320);

        stage.setScene(Style.creerScene(new StackPane(boite), 520, 460));
    }
}
