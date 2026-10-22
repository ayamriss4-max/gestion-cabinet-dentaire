package util;
import modele.Administrateur;
import modele.Patient;
import modele.Utilisateur;
import persistance.UtilisateurPersistance;
import util.MotDePasse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestConsole {

    private static final String DOSSIER = "donnees_test";
    private static int reussis = 0;
    private static int echecs = 0;

    private static void verifier(String nomTest, boolean condition) {
        if (condition) {
            System.out.println("[OK] " + nomTest);
            reussis++;
        } else {
            System.out.println("[ECHEC] " + nomTest);
            echecs++;
        }
    }
    private static Utilisateur connecter(List<Utilisateur> comptes, String login, String pwd) {
        for (Utilisateur u : comptes) {
            if (u.authentifier(login, pwd)) {
                return u;
            }
        }
        return null;
    }

    private static Patient chercherParCin(List<Utilisateur> comptes, String cin) {
        for (Utilisateur u : comptes) {
            if (u instanceof Patient && ((Patient) u).getCin().equalsIgnoreCase(cin)) {
                return (Patient) u;
            }
        }
        return null;
    }

    private static boolean fichierContient(String texte) {
        try {
            for (String ligne : Files.readAllLines(Paths.get(DOSSIER, "utilisateurs.csv"),
                    StandardCharsets.UTF_8)) {
                if (ligne.contains(texte)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
       
        new File(DOSSIER + "/utilisateurs.csv").delete();

        List<Utilisateur> comptes = new ArrayList<>();
        comptes.add(new Administrateur("admin", MotDePasse.hacher("admin123")));

        System.out.println("=== Authentification ===");
        verifier("Admin : connexion valide",
                connecter(comptes, "admin", "admin123") instanceof Administrateur);
        verifier("Admin : mauvais mot de passe", connecter(comptes, "admin", "faux") == null);
        verifier("Login inconnu", connecter(comptes, "inconnu", "abc") == null);

        System.out.println("=== Ajout et recherche d'un patient ===");
        Patient p = new Patient("youssef", MotDePasse.hacher("secret1"), "AB123456",
                "Alami", "Youssef", "Rabat", "0612345678", LocalDate.of(2002, 3, 14));
        comptes.add(p);
        UtilisateurPersistance.sauvegarder(comptes, DOSSIER);

        verifier("Patient : connexion valide",
                connecter(comptes, "youssef", "secret1") instanceof Patient);
        verifier("Patient : mauvais mot de passe", connecter(comptes, "youssef", "nope") == null);
        verifier("Recherche par CIN", chercherParCin(comptes, "AB123456") != null);
        verifier("Recherche d'un CIN inconnu", chercherParCin(comptes, "ZZ000000") == null);

        System.out.println("=== Persistance ===");
        List<Utilisateur> rechargee = UtilisateurPersistance.charger(DOSSIER);
        verifier("Rechargement : patient retrouvé", chercherParCin(rechargee, "AB123456") != null);
        verifier("Rechargement : connexion encore valide",
                connecter(rechargee, "youssef", "secret1") != null);
        verifier("Mot de passe absent du fichier (haché)", !fichierContient("secret1"));

        System.out.println();
        System.out.println("Résultat : " + reussis + " réussis, " + echecs + " échecs");
    }
}