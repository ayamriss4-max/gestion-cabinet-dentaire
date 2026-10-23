package app.cabinet.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerte {

    public static void info(String message) {
        afficher(Alert.AlertType.INFORMATION, "Information", message);
    }

    public static void erreur(String message) {
        afficher(Alert.AlertType.ERROR, "Erreur", message);
    }

   
    public static boolean confirmer(String message) {
        Alert alerte = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alerte.setTitle("Confirmation");
        alerte.setHeaderText(null);
        return alerte.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private static void afficher(Alert.AlertType type, String titre, String message) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
}