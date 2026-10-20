package app.cabinet.modele;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Creneau {
    private int idCreneau;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String type; 
    private Dentiste dentiste;
    private boolean libre;

    public Creneau() {
        this.libre = true;
    }

    public Creneau(int idCreneau, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String type, Dentiste dentiste) {
        if (heureDebut != null && heureFin != null && !heureDebut.isBefore(heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être strictement antérieure à l'heure de fin.");
        }
        this.idCreneau = idCreneau;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.dentiste = dentiste;
        this.libre = true;
    }

    public boolean estLibre() {
        return this.libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }
    public boolean contient(Acte acte) {
        if (acte == null) {
            return false;
        }
        long dureeCreneauMinutes = ChronoUnit.MINUTES.between(heureDebut, heureFin);
        return dureeCreneauMinutes >= acte.getDuree();
    }

    public int getIdCreneau() {
        return idCreneau;
    }

    public void setIdCreneau(int idCreneau) {
        this.idCreneau = idCreneau;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Dentiste getDentiste() {
        return dentiste;
    }

    public void setDentiste(Dentiste dentiste) {
        this.dentiste = dentiste;
    }

    @Override
    public String toString() {
        return "Creneau [" + date + " | " + heureDebut + " - " + heureFin + " | Type: " + type + " | Libre: " + libre + "]";
    }
}
