package app.cabinet.vue;


import app.cabinet.modele.Creneau;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlanningView extends BorderPane {

    private final PlanningGrid grille = new PlanningGrid();
    private final Label labelSemaine = new Label();
    private List<Creneau> creneaux = new ArrayList<>();
    private LocalDate lundi = lundiDe(LocalDate.now());

    public PlanningView() {
        Button btnPrecedente = new Button("< Semaine précédente");
        Button btnAujourdhui = new Button("Aujourd'hui");
        Button btnSuivante = new Button("Semaine suivante >");

        btnPrecedente.setOnAction(e -> changerSemaine(-1));
        btnSuivante.setOnAction(e -> changerSemaine(1));
        btnAujourdhui.setOnAction(e -> {
            lundi = lundiDe(LocalDate.now());
            afficher();
        });

        labelSemaine.setStyle("-fx-font-weight: bold;");

        HBox barre = new HBox(10, btnPrecedente, btnAujourdhui, btnSuivante, labelSemaine);
        barre.setAlignment(Pos.CENTER_LEFT);
        barre.setPadding(new Insets(10));
        setTop(barre);
        setCenter(new ScrollPane(grille));

        afficher();
    }

    /** Fournit les creneaux (d'un seul dentiste) a afficher. */
    public void setCreneaux(List<? extends Creneau> liste) {
        this.creneaux = new ArrayList<>(liste);
        afficher();
    }

    public ObjectProperty<Creneau> selectionProperty() {
        return grille.selectionProperty();
    }

    public void rafraichir() {
        grille.rafraichir();
    }

    private void changerSemaine(int nombre) {
        lundi = lundi.plusWeeks(nombre);
        afficher();
    }

    private void afficher() {
        grille.afficher(creneaux, lundi);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
        labelSemaine.setText("Semaine du " + lundi.format(f)
                + " au " + lundi.plusDays(5).format(f));
    }

    private static LocalDate lundiDe(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}