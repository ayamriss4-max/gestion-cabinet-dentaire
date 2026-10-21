package app.cabinet.modele;

public class Acte {
    private int id;
    private String libelle;
    private double tarif;

    public Acte(int id, String libelle, double tarif) {
        this.id = id;
        this.libelle = libelle;
        this.tarif = tarif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }
}