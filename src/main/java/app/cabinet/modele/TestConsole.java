package app.cabinet.modele;

import app.cabinet.modele.*;
import app.cabinet.persistance.*;
import app.cabinet.vue.PdfExporter; // Import corrigé

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class TestConsole {

    private static int reussis = 0;
    private static int echoues = 0;

    private static void verifier(String description, boolean condition) {
        if (condition) {
            reussis++;
            System.out.println("  [OK]    " + description);
        } else {
            echoues++;
            System.out.println("  [ECHEC] " + description);
        }
    }

    public static void main(String[] args) {

        // Vérifiez que ces constructeurs existent dans Dentiste et Patient
        Dentiste dentiste = new Dentiste("Alaoui", "Karim");
        Patient patient = new Patient("Bennani", "Sara");
        Acte acte = new Acte("Détartrage", 30, 300.0);

        Creneau c1 = new Creneau(1, LocalDate.of(2026, 10, 26),
                LocalTime.of(9, 0), LocalTime.of(10, 0), Creneau.NORMAL, dentiste);
        Creneau c2 = new Creneau(2, LocalDate.of(2026, 10, 26),
                LocalTime.of(10, 0), LocalTime.of(10, 20), Creneau.URGENCE, dentiste);

        System.out.println("Scenario 1 : reservation");
        verifier("Le creneau c1 est libre au depart", c1.estLibre());
        verifier("L'acte (30 min) tient dans c1 (60 min)", c1.contient(acte));

        CreneauReserve cr1 = new CreneauReserve(c1);
        RendezVous rdv1 = new RendezVous(1, new Date(), patient, acte, cr1);
        verifier("Le RDV commence a l'etat 'en attente'", rdv1.getEtat().equals(RendezVous.EN_ATTENTE));
        verifier("Le creneau reserve n'est plus libre", !cr1.estLibre());
        verifier("Le creneau reserve connait son RDV", cr1.getRendezVous() == rdv1);

        System.out.println("Scenario 2 : validation et annulation");
        rdv1.valider();
        verifier("Apres valider(), l'etat est 'valide'", rdv1.getEtat().equals(RendezVous.VALIDE));
        verifier("Le creneau reste occupe apres validation", !cr1.estLibre());

        rdv1.annuler();
        verifier("Apres annuler(), l'etat est 'annule'", rdv1.getEtat().equals(RendezVous.ANNULE));
        verifier("Le creneau redevient libre apres annulation", cr1.estLibre());

        boolean erreur = false;
        try {
            rdv1.valider();
        } catch (IllegalStateException e) {
            erreur = true;
        }
        verifier("Valider un RDV annule est refuse", erreur);

        System.out.println("Scenario 3 : double reservation et duree");
        CreneauReserve cr2 = new CreneauReserve(c2);
        RendezVous rdv2 = new RendezVous(2, new Date(), patient, acte, cr2);

        erreur = false;
        try {
            new CreneauReserve(cr2);
        } catch (IllegalStateException e) {
            erreur = true;
        }
        verifier("Une double reservation est refusee", erreur);
        verifier("L'acte (30 min) ne tient pas dans c2 (20 min)", !c2.contient(acte));

        System.out.println("Scenario 4 : sauvegarde et rechargement");
        List<RendezVous> liste = new ArrayList<>();
        liste.add(rdv1);
        liste.add(rdv2);

        RdvDao dao = new RdvDao();
        dao.sauvegarder(liste);
        List<RendezVous> rechargee = dao.charger();

        verifier("Deux rendez-vous sont recharges", rechargee.size() == 2);
        verifier("L'etat du RDV 1 est conserve (annule)",
                rechargee.get(0).getEtat().equals(RendezVous.ANNULE));
        verifier("Le RDV 2 est conserve et son creneau reste occupe",
                rechargee.get(1).getEtat().equals(RendezVous.EN_ATTENTE)
                        && !rechargee.get(1).getCreneau().estLibre());

        // ===== Scenario 5 : date passee, fiche et PDF =====
        System.out.println("Scenario 5 : date passee, fiche et PDF");
        Creneau passe = new Creneau(3, LocalDate.now().minusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0), Creneau.NORMAL, dentiste);
        erreur = false;
        try {
            new CreneauReserve(passe);
        } catch (IllegalStateException e) {
            erreur = true;
        }
        verifier("Reserver un creneau passe est refuse", erreur);
        verifier("La fiche contient le nom du patient",
                rdv2.genererFiche().contains("Bennani"));
        verifier("La fiche contient l'etat du RDV",
                rdv2.genererFiche().contains(rdv2.getEtat()));

        File pdf = new File("data/fiche.pdf");
        try {
            PdfExporter.exporter(rdv2, pdf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verifier("Le PDF de la fiche est cree", pdf.exists() && pdf.length() > 0);

        System.out.println();
        System.out.println("Bilan : " + reussis + " reussis, " + echoues + " echoues");
    }
}