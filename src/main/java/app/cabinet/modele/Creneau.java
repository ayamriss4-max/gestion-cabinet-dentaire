package app.cabinet.modele;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class Creneau implements Serializable {

    private int idCreneau;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String type;
    private Dentiste dentiste;
    
    public static String NORMAL= "normal" ;
    public static String URGENCE= "urgence" ;
    
    public Creneau(int idCreneau, LocalDate date, LocalTime heureDebut,
                   LocalTime heureFin, String type, Dentiste dentiste) {
        if (!heureDebut.isBefore(heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit précéder l'heure de fin");
        }
        this.idCreneau = idCreneau;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.dentiste = dentiste;
    }

    public int getIdCreneau() { return idCreneau; }
    public void setIdCreneau(int idCreneau) { this.idCreneau = idCreneau; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Dentiste getDentiste() { return dentiste; }
    public void setDentiste(Dentiste dentiste) { this.dentiste = dentiste; }
    public boolean estLibre() {
        return true;
    }

    public boolean contient(Acte acte) {
        long minutesCreneau = Duration.between(heureDebut, heureFin).toMinutes();
        long minutesActe = acte.getDuree().toMinutes();
        return minutesCreneau <= minutesActe;
    }
    
    

    public boolean estPasse() {
        return LocalDateTime.of(date, heureDebut).isBefore(LocalDateTime.now());
    }


    @Override
    public String toString() {
        return date + " " + heureDebut + "-" + heureFin +  type ;
    }
}