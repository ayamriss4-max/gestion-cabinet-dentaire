package app.cabinet.modele;

public class Creneau {
    private int id;
    private String dateHeure; 
    private int dureeMinutes; 
    private int dentisteId;   

    public Creneau(int id, String dateHeure, int dureeMinutes, int dentisteId) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.dureeMinutes = dureeMinutes;
        this.dentisteId = dentisteId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDateHeure() { return dateHeure; }
    public void setDateHeure(String dateHeure) { this.dateHeure = dateHeure; }

    public int getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(int dureeMinutes) { this.dureeMinutes = dureeMinutes; }

    public int getDentisteId() { return dentisteId; }
    public void setDentisteId(int dentisteId) { this.dentisteId = dentisteId; }
}