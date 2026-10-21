package app.cabinet.vue;

import app.cabinet.modele.Creneau;
import app.cabinet.modele.Dentiste;
import app.cabinet.persistance.CabinetDao;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class MainController {

    @FXML private ListView<String> dentisteListView;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField specialiteField;
    private List<Dentiste> listeDentistes;

    @FXML private ListView<String> creneauListView;
    @FXML private TextField dateHeureField;
    @FXML private TextField dureeField;
    @FXML private TextField dentisteIdField;
    private List<Creneau> listeCreneaux;

    @FXML
    private void handleChargerDentistes() {
        listeDentistes = CabinetDao.lireDentistes();
        dentisteListView.getItems().clear();
        for (Dentiste d : listeDentistes) {
            dentisteListView.getItems().add("Dr. " + d.getPrenom() + " " + d.getNom() + " - " + d.getSpecialite());
        }
    }

    @FXML
    private void handleAjouterDentiste() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String specialite = specialiteField.getText().trim();

        if (!nom.isEmpty() && !prenom.isEmpty() && !specialite.isEmpty()) {
            if (listeDentistes == null) listeDentistes = CabinetDao.lireDentistes();
            int nouveauId = listeDentistes.size() + 1;
            listeDentistes.add(new Dentiste(nouveauId, nom, prenom, specialite));
            CabinetDao.sauvegarderDentistes(listeDentistes, "dentistes.txt");

            nomField.clear();
            prenomField.clear();
            specialiteField.clear();
            handleChargerDentistes();
        }
    }

    @FXML
    private void handleChargerCreneaux() {
        listeCreneaux = CabinetDao.lireCreneaux();
        creneauListView.getItems().clear();
        for (Creneau c : listeCreneaux) {
            creneauListView.getItems().add("Créneau #" + c.getId() + " | Date: " + c.getDateHeure() + " (" + c.getDureeMinutes() + " min) - Dentiste ID: " + c.getDentisteId());
        }
    }

    @FXML
    private void handleAjouterCreneau() {
        String dateHeure = dateHeureField.getText().trim();
        String dureeStr = dureeField.getText().trim();
        String dentisteIdStr = dentisteIdField.getText().trim();

        if (!dateHeure.isEmpty() && !dureeStr.isEmpty() && !dentisteIdStr.isEmpty()) {
            try {
                if (listeCreneaux == null) listeCreneaux = CabinetDao.lireCreneaux();
                int nouveauId = listeCreneaux.size() + 1;
                int duree = Integer.parseInt(dureeStr);
                int dentisteId = Integer.parseInt(dentisteIdStr);

                listeCreneaux.add(new Creneau(nouveauId, dateHeure, duree, dentisteId));
                CabinetDao.sauvegarderCreneaux(listeCreneaux, "creneaux.txt");

                dateHeureField.clear();
                dureeField.clear();
                dentisteIdField.clear();
                handleChargerCreneaux();
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Gérer les erreurs de saisie numérique si besoin
            }
        }
    }
}