package app.cabinet.vue;

import app.cabinet.modele.Dentiste;
import app.cabinet.persistance.CabinetDao;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class MainController {

    @FXML
    private ListView<String> dentisteListView;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField specialiteField;

    private List<Dentiste> listeDentistes;

    @FXML
    private void handleChargerDentistes() {
        listeDentistes = CabinetDao.lireDentistes();
        dentisteListView.getItems().clear();
        
        for (Dentiste d : listeDentistes) {
            dentisteListView.getItems().add("Dr. " + d.getPrenom() + " " + d.getNom() + " - Spécialité: " + d.getSpecialite());
        }
    }

    @FXML
    private void handleAjouterDentiste() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String specialite = specialiteField.getText().trim();

        if (!nom.isEmpty() && !prenom.isEmpty() && !specialite.isEmpty()) {
            if (listeDentistes == null) {
                listeDentistes = CabinetDao.lireDentistes();
            }
            
            int nouveauId = listeDentistes.size() + 1;
            Dentiste nouveauDentiste = new Dentiste(nouveauId, nom, prenom, specialite);
            
            listeDentistes.add(nouveauDentiste);
            CabinetDao.sauvegarderDentistes(listeDentistes, "dentistes.txt");

            nomField.clear();
            prenomField.clear();
            specialiteField.clear();
            handleChargerDentistes();
        }
    }
}