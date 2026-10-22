package ui;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class Style {

    public static Scene creerScene(Parent racine, double largeur, double hauteur) {
        Scene scene = new Scene(racine, largeur, hauteur);
        scene.getStylesheets().add(Style.class.getResource("/style.css").toExternalForm());
        return scene;
    }
}