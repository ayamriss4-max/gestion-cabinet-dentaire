package app.cabinet.persistance;

import app.cabinet.modele.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RdvDao {

    private static final String DOSSIER = "data";
    private static final String FICHIER = DOSSIER + "/rdv.dat";

    public void sauvegarder(List<RendezVous> rdvs) {
        try {
            Files.createDirectories(Paths.get(DOSSIER));
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FICHIER))) {
                out.writeObject(new ArrayList<>(rdvs));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur de sauvegarde des rendez-vous", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<RendezVous> charger() {
        File f = new File(FICHIER);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<RendezVous>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur de chargement des rendez-vous", e);
        }
    }
}