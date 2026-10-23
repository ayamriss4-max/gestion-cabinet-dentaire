package app.cabinet.service;

import app.cabinet.modele.Acte;
import app.cabinet.modele.Administrateur;
import app.cabinet.modele.Creneau;
import app.cabinet.modele.CreneauReserve;
import app.cabinet.modele.Dentiste;
import app.cabinet.modele.Patient;
import app.cabinet.modele.RendezVous;
import app.cabinet.modele.Utilisateur;
import app.cabinet.persistance.UtilisateurPersistance;
import app.cabinet.util.MotDePasse;
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

    public static void chargerTout() {
        utilisateurs.clear();
        utilisateurs.addAll(UtilisateurPersistance.charger(dossier));
        if (utilisateurs.isEmpty()) {
            utilisateurs.add(new Administrateur("admin", MotDePasse.hacher("admin123")));
            sauvegarderUtilisateurs();
        }
    }

    public static void sauvegarderUtilisateurs() {
        UtilisateurPersistance.sauvegarder(utilisateurs, dossier);
    }

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

    public static Patient chercherPatientParCin(String cin) {
        for (Patient p : getPatients()) {
            if (p.getCin().equalsIgnoreCase(cin)) {
                return p;
            }
        }
        return null;
    }

    public static boolean patientADesRdv(Patient p) {
        return !rdvsDuPatient(p).isEmpty();
    }

    // ---------- Rendez-vous ----------

    public static RendezVous rdvDuCreneau(Creneau c) {
        for (RendezVous r : rdvs) {
            if (r.getCreneau().getIdCreneau() == c.getIdCreneau()
                    && !r.getEtat().equals(RendezVous.ANNULE)) {
                return r;
            }
        }
        return null;
    }

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
                    // CORRECTION ICI : On enveloppe le créneau dans un CreneauReserve
                    CreneauReserve cr = new CreneauReserve(c);
                    rdvs.add(new RendezVous(Integer.parseInt(t[0]), LocalDate.parse(t[1]),
                            LocalDate.parse(t[2]), t[3], (Patient) u, a, cr));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture de rdv.csv : " + e.getMessage());
        }
    }
}