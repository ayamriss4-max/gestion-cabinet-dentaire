package app.cabinet.modele;

import java.util.ArrayList;
import java.util.List;

public class Dentiste {
    private int idDentiste;
    private String nom;
    private String prenom;
    private String specialite; 
    private List<Creneau> planning;

    public Dentiste() {
        this.planning = new ArrayList<>();
    }
    public Dentiste(String nom, String prenom) {
        this(0, nom, prenom, "");
    }

    public Dentiste(int idDentiste, String nom, String prenom, String specialite) {
        this.idDentiste = idDentiste;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.planning = new ArrayList<>();
    }

    public List<Creneau> getPlanning() {
        return planning;
    }

    public void setPlanning(List<Creneau> planning) {
        this.planning = planning;
    }

    public void ajouterCreneau(Creneau c) {
        this.planning.add(c);
    }

    public int getId() {
        return idDentiste;
    }

    public int getIdDentiste() {
        return idDentiste;
    }

    public void setIdDentiste(int idDentiste) {
        this.idDentiste = idDentiste;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Dr. " + prenom + " " + nom + " (" + specialite + ")";
    }
}