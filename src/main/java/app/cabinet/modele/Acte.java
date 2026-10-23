package app.cabinet.modele;

public class Acte {
    private int id;
    private String libelle;
    private int duree; // Ajout de la durée
    private double tarif;

    // Constructeur mis à jour pour correspondre à TestConsole
    public Acte(String libelle, int duree, double tarif) {
        this.libelle = libelle;
        this.duree = duree;
        this.tarif = tarif;
    }
    public Acte(int id, String libelle, double tarif) {
        this.id = id;
        this.libelle = libelle;
        this.duree = 30; // Valeur par défaut si non fournie
        this.tarif = tarif;
    }
    public int getIdActe() {
        return this.id;
    }
    // Constructeur alternatif avec ID
    public Acte(int id, String libelle, int duree, double tarif) {
        this.id = id;
        this.libelle = libelle;
        this.duree = duree;
        this.tarif = tarif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public int getDuree() { return duree; } // Méthode manquante
    public void setDuree(int duree) { this.duree = duree; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }
    
    @Override
    public String toString() {
        return libelle;
    }
}