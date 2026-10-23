package app.cabinet.vue; // Vérifiez votre package

import app.cabinet.modele.RendezVous;
import app.cabinet.service.Cabinet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class RdvView extends BorderPane {

    private final TableView<RendezVous> table = new TableView<>();
    private final FilteredList<RendezVous> filtre =
            new FilteredList<>(Cabinet.getInstance().getRdvs(), r -> true);
    private final ComboBox<String> comboEtat = new ComboBox<>();

    public RdvView() {
        construireTable();
        construireBarreHaut();
        construireBarreBas();
        setCenter(table);
    }

    private void construireTable() {
        TableColumn<RendezVous, String> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(c ->
            new SimpleStringProperty(String.valueOf(c.getValue().getPatient())));

        TableColumn<RendezVous, String> colDentiste = new TableColumn<>("Dentiste");
        colDentiste.setCellValueFactory(c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getCreneau().getDentiste())));

        TableColumn<RendezVous, String> colActe = new TableColumn<>("Acte");
        colActe.setCellValueFactory(c ->
            new SimpleStringProperty(String.valueOf(c.getValue().getActe())));

        TableColumn<RendezVous, String> colDate = new TableColumn<>("Date et heure");
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getCreneau().getDate() + " "
            + c.getValue().getCreneau().getHeureDebut()));

        TableColumn<RendezVous, String> colEtat = new TableColumn<>("Etat");
        colEtat.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getEtat()));

        table.getColumns().addAll(colPatient, colDentiste, colActe, colDate, colEtat);

        SortedList<RendezVous> tri = new SortedList<>(filtre);
        tri.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(tri);
    }

    private void construireBarreHaut() {
        comboEtat.getItems().addAll("Tous", RendezVous.EN_ATTENTE,
                RendezVous.VALIDE, RendezVous.ANNULE);
        comboEtat.setValue("Tous");
        comboEtat.setOnAction(e -> appliquerFiltre());

        HBox barre = new HBox(10, new Label("Etat :"), comboEtat);
        barre.setPadding(new Insets(10));
        setTop(barre);
    }

    private void appliquerFiltre() {
        String choix = comboEtat.getValue();
        filtre.setPredicate(r -> "Tous".equals(choix)
                || r.getEtat().equals(choix));
    }

    private void construireBarreBas() {
        Button btnValider = new Button("Valider");
        Button btnAnnuler = new Button("Annuler le rendez-vous");

        btnValider.disableProperty().bind(
            table.getSelectionModel().selectedItemProperty().isNull());
        btnAnnuler.disableProperty().bind(
            table.getSelectionModel().selectedItemProperty().isNull());

        btnValider.setOnAction(e -> valider());
        btnAnnuler.setOnAction(e -> annuler());

        HBox barre = new HBox(10, btnValider, btnAnnuler);
        barre.setPadding(new Insets(10));
        setBottom(barre);
    }

    private void valider() {
        RendezVous r = table.getSelectionModel().getSelectedItem();
        try {
            r.valider();
            miseAJour();
        } catch (IllegalStateException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        }
    }

    private void annuler() {
        RendezVous r = table.getSelectionModel().getSelectedItem();
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                "Annuler ce rendez-vous ?");
        conf.showAndWait().ifPresent(rep -> {
            if (rep == ButtonType.OK) {
                r.annuler();
                miseAJour();
            }
        });
    }

    private void miseAJour() {
        Cabinet.getInstance().sauvegarderTout();
        appliquerFiltre();
        table.refresh();
    }
}