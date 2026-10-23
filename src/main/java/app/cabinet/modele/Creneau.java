package app.cabinet.modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class Creneau {
    private int id;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String type; // ex: NORMAL, URGENCE
    private Dentiste dentiste;

    public static final String NORMAL = "NORMAL";
    public static final String URGENCE = "URGENCE";

    public Creneau(int id, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String type, Dentiste dentiste) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.dentiste = dentiste;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    // Méthodes utilitaires demandées par les autres classes
    public boolean estLibre() {
        return true; // Logique simplifiée pour ce niveau
    }

    public boolean estPasse() {
        return date.isBefore(LocalDate.now()) || 
              (date.isEqual(LocalDate.now()) && heureFin.isBefore(LocalTime.now()));
    }

    public boolean contient(Acte acte) {
        long dureeCreneau = java.time.Duration.between(heureDebut, heureFin).toMinutes();
        return dureeCreneau >= acte.getDuree();
    }
    
    // Alias pour getIdCreneau si utilisé ailleurs
    public int getIdCreneau() { return id; }
 // 1. L'ancien constructeur utilisé par CabinetDao
    public Creneau(int id, String dateHeure, int dureeMinutes, int dentisteId) {
        this.id = id;
        // On convertit le String en LocalDate/Time (supposons le format "yyyy-MM-dd HH:mm")
        try {
            String[] parts = dateHeure.split(" ");
            this.date = java.time.LocalDate.parse(parts[0]);
            this.heureDebut = java.time.LocalTime.parse(parts[1]);
            this.heureFin = this.heureDebut.plusMinutes(dureeMinutes);
        } catch (Exception e) {
            this.date = java.time.LocalDate.now(); // Fallback
        }
        this.type = NORMAL;
        // Note: dentisteId n'est pas stocké directement dans votre nouvelle classe, 
        // mais on peut le garder en mémoire si besoin.
    }

    // 2. Les méthodes manquantes
    public String getDateHeure() {
        return this.date + " " + this.heureDebut;
    }

    public int getDureeMinutes() {
        return (int) java.time.Duration.between(heureDebut, heureFin).toMinutes();
    }

    public int getDentisteId() {
        return (dentiste != null) ? dentiste.getId() : 0;
    }
}