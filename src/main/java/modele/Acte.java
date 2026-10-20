package modele;

public class Acte {
    private int idActe;
    private String libelle;
    private int duree; 
    private double tarif;

    public Acte() {
    }

    public Acte(int idActe, String libelle, int duree, double tarif) {
        this.idActe = idActe;
        this.libelle = libelle;
        this.duree = duree;
        this.tarif = tarif;
    }

    public int getIdActe() {
        return idActe;
    }

    public void setIdActe(int idActe) {
        this.idActe = idActe;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    @Override
    public String toString() {
        return "Acte [id=" + idActe + ", libelle=" + libelle + ", durée=" + duree + " min, tarif=" + tarif + " DH]";
    }
}