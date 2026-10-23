package app.cabinet.persistance;

import app.cabinet.modele.Administrateur;
import app.cabinet.modele.Patient;
import app.cabinet.modele.Utilisateur;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurPersistance {

    private static final String FICHIER = "utilisateurs.csv";

    // Écrit tous les comptes dans dossier/utilisateurs.csv
    public static void sauvegarder(List<Utilisateur> utilisateurs, String dossier) {
        List<String> lignes = new ArrayList<>();
        for (Utilisateur u : utilisateurs) {
            if (u instanceof Administrateur) {
                lignes.add("ADMIN;" + u.getLogin() + ";" + u.getPwd());
            } else if (u instanceof Patient) {
                Patient p = (Patient) u;
                lignes.add("PATIENT;" + p.getLogin() + ";" + p.getPwd() + ";" + nettoyer(p.getCin()) + ";" + nettoyer(p.getNom()) + ";" + nettoyer(p.getPrenom()) + ";" + nettoyer(p.getAdresse()) + ";" + nettoyer(p.getTelephone()) + ";" + p.getDateNaissance());
            }
        }
        try {
            Files.createDirectories(Paths.get(dossier));
            Files.write(Paths.get(dossier, FICHIER), lignes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Erreur d'écriture de " + FICHIER + " : " + e.getMessage());
        }
    }

    // Lit les comptes ; renvoie une liste vide si le fichier n'existe pas encore
    public static List<Utilisateur> charger(String dossier) {
        List<Utilisateur> liste = new ArrayList<>();
        Path chemin = Paths.get(dossier, FICHIER);
        if (!Files.exists(chemin)) {
            return liste;
        }
        try {
            for (String ligne : Files.readAllLines(chemin, StandardCharsets.UTF_8)) {
                if (ligne.trim().isEmpty()) { continue; }
                String[] t = ligne.split(";", -1);
                if (t[0].equals("ADMIN") && t.length >= 3) {
                    liste.add(new Administrateur(t[1], t[2]));
                } else if (t[0].equals("PATIENT") && t.length >= 9) {
                    liste.add(new Patient(t[1], t[2], t[3], t[4], t[5], t[6], t[7], LocalDate.parse(t[8])));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture de " + FICHIER + " : " + e.getMessage());
        }
        return liste;
    }

    // Le séparateur du CSV est le point-virgule : on le remplace dans les textes saisis
    private static String nettoyer(String texte) {
        return texte.replace(";", " ");
    }
}