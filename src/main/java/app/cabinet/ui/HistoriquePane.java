package app.cabinet.ui;

import app.cabinet.modele.Patient;
import app.cabinet.modele.RendezVous;
import app.cabinet.service.Donnee;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class HistoriquePane extends VBox {

    private Patient patient;
    private TableView<RendezVous> table = new TableView<>();

    public HistoriquePane(Patient patient) {
        this.patient = patient;
        setPadding(new Insets(10));

        TableColumn<RendezVous, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getDateRdv().toString()));

        TableColumn<RendezVous, String> colHeure = new TableColumn<>("Heure");
        colHeure.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getCreneau().getHeureDebut().toString()));

        TableColumn<RendezVous, String> colDentiste = new TableColumn<>("Dentiste");
        colDentiste.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getCreneau().getDentiste().toString()));

        TableColumn<RendezVous, String> colActe = new TableColumn<>("Acte");
        colActe.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getActe().getLibelle()));

        TableColumn<RendezVous, String> colTarif = new TableColumn<>("Tarif (DH)");
        colTarif.setCellValueFactory(cd -> new SimpleStringProperty(
                String.valueOf(cd.getValue().getActe().getTarif())));

        TableColumn<RendezVous, String> colEtat = new TableColumn<>("État");
        colEtat.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getEtat()));

        // --- CORRECTION DE LA COLONNE PERIODE ---
        TableColumn<RendezVous, String> colPeriode = new TableColumn<>("Période");
        colPeriode.setCellValueFactory(cd -> {
            // On récupère la date du RDV (qui est un java.util.Date)
            Date dateRdv = cd.getValue().getDateRdv();
            
            // On convertit la date d'aujourd'hui (LocalDate) en java.util.Date pour pouvoir comparer
            Date aujourdhui = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // On compare : si la date du RDV est avant aujourd'hui, c'est le passé
            if (dateRdv.before(aujourdhui)) {
                return new SimpleStringProperty("Passé");
            } else {
                return new SimpleStringProperty("À venir");
            }
        });

        table.getColumns().add(colDate);
        table.getColumns().add(colHeure);
        table.getColumns().add(colDentiste);
        table.getColumns().add(colActe);
        table.getColumns().add(colTarif);
        table.getColumns().add(colEtat);
        table.getColumns().add(colPeriode);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getChildren().add(table);
        rafraichir();
    }

    // Recharge la liste (appelée quand on ouvre l'onglet)
    public void rafraichir() {
        List<RendezVous> liste = Donnee.rdvsDuPatient(patient);
        // Tri du plus récent au plus ancien
        liste.sort((a, b) -> b.getDateRdv().compareTo(a.getDateRdv())); 
        table.setItems(FXCollections.observableArrayList(liste));
    }
}