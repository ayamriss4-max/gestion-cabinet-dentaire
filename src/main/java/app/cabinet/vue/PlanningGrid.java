package app.cabinet.vue;

import app.cabinet.modele.Creneau;
import app.cabinet.service.Cabinet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlanningGrid extends GridPane {

    // Code couleur du sujet
    public static final String VERT = "#2e9e5b";     // creneau libre
    public static final String ROUGE = "#d64545";    // creneau occupe
    public static final String ORANGE = "#f08c1a";   // creneau d'urgence
    public static final String BLEU = "#2f6fdb";     // creneau choisi par l'utilisateur

    private static final LocalTime DEBUT = LocalTime.of(8, 0);
    private static final LocalTime FIN = LocalTime.of(18, 0);
    private static final int PAS = 30;       // minutes par ligne
    private static final int NB_JOURS = 6;   // lundi a samedi

    private final ObjectProperty<Creneau> selection = new SimpleObjectProperty<>();
    private final Map<Creneau, Button> boutons = new HashMap<>();

    public PlanningGrid() {
        setHgap(2);
        setVgap(2);
        setPadding(new Insets(10));

        // Le bleu suit la selection
        selection.addListener((obs, ancien, nouveau) -> rafraichir());
        // Toute sauvegarde (reservation, validation, annulation) recolore la grille
        Cabinet.getInstance().modificationsProperty()
                .addListener((obs, ancien, nouveau) -> rafraichir());
    }

    public ObjectProperty<Creneau> selectionProperty() {
        return selection;
    }

    /** Affiche la semaine commencant au lundi donne, avec les creneaux fournis. */
    public void afficher(List<? extends Creneau> creneaux, LocalDate lundi) {
        getChildren().clear();
        boutons.clear();
        selection.set(null);
        construireEntetes(lundi);
        for (Creneau c : creneaux) {
            placer(c, lundi);
        }
        rafraichir();
    }

    /** Recalcule la couleur de chaque case. */
    public void rafraichir() {
        for (Map.Entry<Creneau, Button> e : boutons.entrySet()) {
            e.getValue().setStyle("-fx-background-color: " + couleur(e.getKey())
                    + "; -fx-text-fill: white; -fx-font-weight: bold;");
        }
    }

    /** Regle de couleur, par ordre de priorite : bleu, rouge, orange, vert. */
    private String couleur(Creneau c) {
        if (c == selection.get()) {
            return BLEU;
        }
        if (!c.estLibre()) {
            return ROUGE;
        }
        if (Creneau.URGENCE.equals(c.getType())) {
            return ORANGE;
        }
        return VERT;
    }

    private void construireEntetes(LocalDate lundi) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRENCH);
        for (int j = 0; j < NB_JOURS; j++) {
            Label l = new Label(lundi.plusDays(j).format(f));
            l.setStyle("-fx-font-weight: bold;");
            add(l, j + 1, 0);
            setHalignment(l, HPos.CENTER);
        }
        int ligne = 1;
        for (LocalTime h = DEBUT; h.isBefore(FIN); h = h.plusMinutes(PAS)) {
            add(new Label(h.toString()), 0, ligne++);
        }
    }

    private void placer(Creneau c, LocalDate lundi) {
        long jour = ChronoUnit.DAYS.between(lundi, c.getDate());
        if (jour < 0 || jour >= NB_JOURS) {
            return;   // hors de la semaine affichee
        }
        if (c.getHeureDebut().isBefore(DEBUT) || !c.getHeureDebut().isBefore(FIN)) {
            return;   // hors des heures d'ouverture
        }

        int ligne = 1 + (int) (Duration.between(DEBUT, c.getHeureDebut()).toMinutes() / PAS);
        int duree = (int) Duration.between(c.getHeureDebut(), c.getHeureFin()).toMinutes();
        int span = Math.max(1, (int) Math.ceil(duree / (double) PAS));

        Button b = new Button(c.getHeureDebut() + " - " + c.getHeureFin());
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMaxHeight(Double.MAX_VALUE);
        b.setOnAction(e -> selection.set(c));
        boutons.put(c, b);
        add(b, (int) jour + 1, ligne, 1, span);
    }
}