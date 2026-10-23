package app.cabinet.service;


import app.cabinet.modele.RendezVous;
import app.cabinet.persistance.RdvDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Cabinet {

    private static Cabinet instance;

    private final RdvDao rdvDao = new RdvDao();
    private final ObservableList<RendezVous> rdvs =
            FXCollections.observableArrayList();
    private final IntegerProperty modifications = new SimpleIntegerProperty(0);        

    // A ajouter quand Salma et Aya ont leurs classes de sauvegarde :
    // patients, dentistes, actes, creneaux (ObservableList + leur Dao)

    private Cabinet() {
        chargerTout();
    }

    public static Cabinet getInstance() {
        if (instance == null) {
            instance = new Cabinet();
        }
        return instance;
    }

    public ObservableList<RendezVous> getRdvs() { return rdvs; }

    public void ajouterRdv(RendezVous r) {
        rdvs.add(r);
        sauvegarderTout();
    }

    public void supprimerRdv(RendezVous r) {
        rdvs.remove(r);
        sauvegarderTout();
    }

    private void chargerTout() {
        rdvs.setAll(rdvDao.charger());
    }

    public IntegerProperty modificationsProperty() {
           return modifications;
    }

    public void sauvegarderTout() {
           rdvDao.sauvegarder(rdvs);
           modifications.set(modifications.get() + 1);   // previent les fenetres
    }
    
}