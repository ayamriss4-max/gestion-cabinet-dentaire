package app.cabinet.vue;

import app.cabinet.modele.Acte;
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

    @FXML private ListView<String> acteListView;
    @FXML private TextField libelleField;
    @FXML private TextField tarifField;
    private List<Acte> listeActes;

    @FXML private TextField patientIdResaField;
    @FXML private TextField dentisteIdResaField;
    @FXML private TextField acteIdResaField;
    @FXML private TextField creneauIdResaField;

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
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleChargerActes() {
        listeActes = CabinetDao.lireActes();
        acteListView.getItems().clear();
        for (Acte a : listeActes) {
            acteListView.getItems().add("Acte #" + a.getId() + " | " + a.getLibelle() + " - " + a.getTarif() + " DH");
        }
    }

    @FXML
    private void handleAjouterActe() {
        String libelle = libelleField.getText().trim();
        String tarifStr = tarifField.getText().trim();

        if (!libelle.isEmpty() && !tarifStr.isEmpty()) {
            try {
                if (listeActes == null) listeActes = CabinetDao.lireActes();
                int nouveauId = listeActes.size() + 1;
                double tarif = Double.parseDouble(tarifStr);

                listeActes.add(new Acte(nouveauId, libelle, tarif));
                CabinetDao.sauvegarderActes(listeActes, "actes.txt");

                libelleField.clear();
                tarifField.clear();
                handleChargerActes();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleReserverAdmin() {
        String patientIdStr = patientIdResaField.getText().trim();
        String dentisteIdStr = dentisteIdResaField.getText().trim();
        String acteIdStr = acteIdResaField.getText().trim();
        String creneauIdStr = creneauIdResaField.getText().trim();

        if (!patientIdStr.isEmpty() && !dentisteIdStr.isEmpty() && !acteIdStr.isEmpty() && !creneauIdStr.isEmpty()) {
            try {
                int patientId = Integer.parseInt(patientIdStr);
                int dentisteId = Integer.parseInt(dentisteIdStr);
                int acteId = Integer.parseInt(acteIdStr);
                int creneauId = Integer.parseInt(creneauIdStr);

                if (listeCreneaux == null) listeCreneaux = CabinetDao.lireCreneaux();
                if (listeActes == null) listeActes = CabinetDao.lireActes();

                Creneau creneauSelectionne = null;
                for (Creneau c : listeCreneaux) {
                    if (c.getId() == creneauId) {
                        creneauSelectionne = c;
                        break;
                    }
                }

                if (creneauSelectionne == null) {
                    System.out.println("❌ Erreur : Créneau introuvable (ID: " + creneauId + ").");
                    return;
                }

                if (creneauSelectionne.getDentisteId() != dentisteId) {
                    System.out.println("❌ Erreur : Ce créneau n'appartient pas au Dentiste ID #" + dentisteId);
                    return;
                }

                Acte acteSelectionne = null;
                for (Acte a : listeActes) {
                    if (a.getId() == acteId) {
                        acteSelectionne = a;
                        break;
                    }
                }

                if (creneauSelectionne.getDureeMinutes() < 15) { // Exemple de règle de durée minimale
                    System.out.println("❌ Erreur : Durée du créneau insuffisante pour effectuer cet acte.");
                    return;
                }

                System.out.println("✅ Réservation validée avec succès pour le patient #" + patientId 
                        + " avec le dentiste #" + dentisteId 
                        + " pour l'acte #" + acteId 
                        + " sur le créneau #" + creneauId);

                patientIdResaField.clear();
                dentisteIdResaField.clear();
                acteIdResaField.clear();
                creneauIdResaField.clear();

            } catch (NumberFormatException e) {
                System.out.println("❌ Erreur : Les ID doivent être des nombres entiers valides.");
            }
        } else {
            System.out.println("⚠️ Veuillez remplir tous les champs de réservation.");
        }
}
}