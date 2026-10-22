package app.cabinet.modele;

import java.io.Serializable;
import java.time.Duration;

public class Acte implements Serializable{
	
    private static final long serialVersionUID = 1L;

    private int id;
    private String libelle;
    private double tarif;
    private Duration duree ;
   
    public Acte(int id, String libelle, double tarif,Duration duree ) {
        this.id = id;
        this.libelle = libelle;
        this.tarif = tarif;
        this.duree= duree ;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public Duration getDuree() {
        return duree ;
    }
}