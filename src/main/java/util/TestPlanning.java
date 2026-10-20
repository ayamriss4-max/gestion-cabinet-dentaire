package util;

import modele.Acte;
import modele.Creneau;
import modele.Dentiste;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestPlanning {
    public static void main(String[] args) {
        System.out.println(" TEST CONSOLE - CABINET DENTAIRE (SEMAINE 1)");

        Dentiste d1 = new Dentiste(1, "Bennani", "Karim", "Chirurgien Dentiste Généraliste");
        System.out.println("Dentiste créé : " + d1);

        Acte acte1 = new Acte(10, "Détartrage", 30, 300.0);
        System.out.println("Acte créé : " + acte1);

        Creneau c1 = new Creneau(1, LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), "normal", d1);
        d1.ajouterCreneau(c1);

        System.out.println("Le créneau est-il libre ? " + c1.estLibre());
        System.out.println("L'acte tient-il dans le créneau ? " + c1.contient(acte1));

        Acte acteLong = new Acte(11, "Opération lourde", 90, 1500.0);
        System.out.println("L'acte long (90 min) tient-il dans le créneau de 60 min ? " + c1.contient(acteLong));
    }
}