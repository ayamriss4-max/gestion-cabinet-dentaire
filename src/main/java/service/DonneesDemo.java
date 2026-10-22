package service;

import modele.Acte;
import modele.Creneau;
import modele.Dentiste;
import modele.Patient;
import util.MotDePasse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class DonneesDemo {

    // Crée les données de démonstration seulement si aucun dentiste n'existe encore
    public static void creerSiVide() {
        if (!Donnee.dentistes.isEmpty()) {
            return;
        }

        Dentiste d1 = new Dentiste(1, "Alaoui", "Karim", "généraliste");
        Dentiste d2 = new Dentiste(2, "Benani", "Sara", "orthodontiste");
        Donnee.dentistes.add(d1);
        Donnee.dentistes.add(d2);

        Donnee.actes.add(new Acte(1, "Détartrage", 30, 200));
        Donnee.actes.add(new Acte(2, "Extraction", 45, 300));
        Donnee.actes.add(new Acte(3, "Dévitalisation", 60, 500));
        Donnee.actes.add(new Acte(4, "Pose d'appareil", 90, 1500));

        // Créneaux des 7 prochains jours, sauf samedi et dimanche
        int[] heures = {9, 10, 11, 14, 15};
        int id = 1;
        for (int j = 0; j < 7; j++) {
            LocalDate jour = LocalDate.now().plusDays(j);
            if (jour.getDayOfWeek() == DayOfWeek.SATURDAY || jour.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }
            for (Dentiste d : Donnee.dentistes) {
                for (int h : heures) {
                    Donnee.creneaux.add(new Creneau(id, jour, LocalTime.of(h, 0),
                            LocalTime.of(h + 1, 0), "normal", d));
                    id++;
                }
                // Un créneau d'urgence de 30 minutes à 16 h
                Donnee.creneaux.add(new Creneau(id, jour, LocalTime.of(16, 0),
                        LocalTime.of(16, 30), "urgence", d));
                id++;
            }
        }

        // Patient de démonstration
        if (Donnee.chercherParLogin("patient1") == null) {
            Donnee.utilisateurs.add(new Patient("patient1", MotDePasse.hacher("patient123"),
                    "AB123456", "Alami", "Youssef", "Rabat", "0612345678", LocalDate.of(2002, 3, 14)));
            Donnee.sauvegarderUtilisateurs();
        }
    }
}