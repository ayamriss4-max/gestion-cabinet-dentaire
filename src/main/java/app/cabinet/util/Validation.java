package app.cabinet.util;

import java.time.LocalDate;

public class Validation {

    // CIN marocain : 1 ou 2 lettres suivies de 5 ou 6 chiffres (ex. AB123456)
    public static boolean cinValide(String cin) {
        return cin != null && cin.matches("[A-Za-z]{1,2}[0-9]{5,6}");
    }

    // Téléphone marocain : 0612345678 ou +212612345678
    public static boolean telephoneValide(String telephone) {
        return telephone != null && telephone.matches("([+]212|0)[5-7][0-9]{8}");
    }

    // Date de naissance obligatoire, dans le passé et après 1900
    public static boolean dateNaissanceValide(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now()) && date.getYear() >= 1900;
    }

    public static boolean textePresent(String texte) {
        return texte != null && !texte.trim().isEmpty();
    }

    public static boolean sansPointVirgule(String texte) {
        return texte == null || !texte.contains(";");
    }
}