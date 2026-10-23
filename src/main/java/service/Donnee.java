package service;

import modele.Acte;
import modele.Administrateur;
import modele.Creneau;
import modele.Dentiste;
import modele.Patient;
import modele.RendezVous;
import modele.Utilisateur;
import persistance.UtilisateurPersistance;
import util.MotDePasse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Donnee {

    // Dossier des fichiers CSV
    public static String dossier = "donnees";

    public static List<Utilisateur> utilisateurs = new ArrayList<>();
    public static List<Dentiste> dentistes = new ArrayList<>();
    public static List<Acte> actes = new ArrayList<>();
    public static List<Creneau> creneaux = new ArrayList<>();
    public static List<RendezVous> rdvs = new ArrayList<>();

    // ---------- Utilisateurs ----------

    // Lit les comptes dans donnees/utilisateurs.csv
    public static void chargerTout() {
        utilisateurs.clear();
        utilisateurs.addAll(UtilisateurPersistance.charger(dossier));
        // Premier lancement : on crée le compte administrateur par défaut
        if (utilisateurs.isEmpty()) {
            utilisateurs.add(new Administrateur("admin", MotDePasse.hacher("admin123")));
            sauvegarderUtilisateurs();
        }
    }

    public static void sauvegarderUtilisateurs() {
        UtilisateurPersistance.sauvegarder(utilisateurs, dossier);
    }

    // Renvoie l'utilisateur connecté, ou null si login ou mot de passe incorrect
    public static Utilisateur connecter(String login, String pwd) {
        for (Utilisateur u : utilisateurs) {
            if (u.authentifier(login, pwd)) {
                return u;
            }
        }
        return null;
    }

    public static List<Patient> getPatients() {
        List<Patient> patients = new ArrayList<>();
        for (Utilisateur u : utilisateurs) {
            if (u instanceof Patient) {
                patients.add((Patient) u);
            }
        }
        return patients;
    }

    public static Utilisateur chercherParLogin(String login) {
        for (Utilisateur u : utilisateurs) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    // Renvoie le patient qui a ce CIN, ou null s'il n'existe pas
    public static Patient chercherPatientParCin(String cin) {
        for (Patient p : getPatients()) {
            if (p.getCin().equalsIgnoreCase(cin)) {
                return p;
            }
        }
        return null;
    }

    // Vrai si le patient a au moins un rendez-vous
    public static boolean patientADesRdv(Patient p) {
        return !rdvsDuPatient(p).isEmpty();
    }

    // ---------- Rendez-vous ----------

    // Le rendez-vous (non annulé) qui occupe ce créneau, ou null si le créneau est libre
    public static RendezVous rdvDuCreneau(Creneau c) {
        for (RendezVous r : rdvs) {
            if (r.getCreneau().getIdCreneau() == c.getIdCreneau()
                    && !r.getEtat().equals(RendezVous.ANNULE)) {
                return r;
            }
        }
        return null;
    }

    // Les rendez-vous d'un patient
    public static List<RendezVous> rdvsDuPatient(Patient p) {
        List<RendezVous> liste = new ArrayList<>();
        for (RendezVous r : rdvs) {
            if (r.getPatient().getLogin().equals(p.getLogin())) {
                liste.add(r);
            }
        }
        return liste;
    }

    public static int prochainIdRdv() {
        int max = 0;
        for (RendezVous r : rdvs) {
            if (r.getIdRdv() > max) {
                max = r.getIdRdv();
            }
        }
        return max + 1;
    }

    private static Acte trouverActe(int id) {
        for (Acte a : actes) {
            if (a.getIdActe() == id) {
                return a;
            }
        }
        return null;
    }

    private static Creneau trouverCreneau(int id) {
        for (Creneau c : creneaux) {
            if (c.getIdCreneau() == id) {
                return c;
            }
        }
        return null;
    }

    // Écrit tous les rendez-vous dans donnees/rdv.csv
    public static void sauvegarderRdvs() {
        List<String> lignes = new ArrayList<>();
        for (RendezVous r : rdvs) {
            lignes.add(r.getIdRdv() + ";" + r.getDateReservation() + ";" + r.getDateRdv() + ";"
                    + r.getEtat() + ";" + r.getPatient().getLogin() + ";"
                    + r.getActe().getIdActe() + ";" + r.getCreneau().getIdCreneau());
        }
        try {
            Files.createDirectories(Paths.get(dossier));
            Files.write(Paths.get(dossier, "rdv.csv"), lignes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Erreur d'écriture de rdv.csv : " + e.getMessage());
        }
    }

    // Lit donnees/rdv.csv (à appeler après la création des actes et des créneaux)
    public static void chargerRdvs() {
        rdvs.clear();
        Path chemin = Paths.get(dossier, "rdv.csv");
        if (!Files.exists(chemin)) {
            return;
        }
        try {
            for (String ligne : Files.readAllLines(chemin, StandardCharsets.UTF_8)) {
                if (ligne.trim().isEmpty()) {
                    continue;
                }
                String[] t = ligne.split(";", -1);
                if (t.length < 7) {
                    continue;
                }
                Utilisateur u = chercherParLogin(t[4]);
                Acte a = trouverActe(Integer.parseInt(t[5]));
                Creneau c = trouverCreneau(Integer.parseInt(t[6]));
                if (u instanceof Patient && a != null && c != null) {
                    rdvs.add(new RendezVous(Integer.parseInt(t[0]), LocalDate.parse(t[1]),
                            LocalDate.parse(t[2]), t[3], (Patient) u, a, c));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture de rdv.csv : " + e.getMessage());
        }
    }
}