package app.cabinet.modele;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import app.cabinet.vue.PdfExporter;

import java.util.Date;

public class RendezVous implements Serializable {


	public static final String ANNULE = "annulé";
    public static final String VALIDE = "validé";
    public static final String EN_ATTENTE = "en attente";
    
    private int idRdv;
    private Date dateReservation;
    private Date dateRdv;
    private String etat;
    private Patient patient;
    private Acte acte;
    private CreneauReserve creneau;

    public RendezVous(int idRdv, Date dateRdv, Patient patient, Acte acte, CreneauReserve creneau) {
        this.idRdv = idRdv;
        this.dateReservation = new Date();   
        this.dateRdv = dateRdv;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
        this.etat = "en attente";              
        creneau.setRendezVous(this);         
    }

    
    public int getIdRdv() { return idRdv; }
    public void setIdRdv(int idRdv) { this.idRdv = idRdv; }

    public Date getDateReservation() { return dateReservation; }
    public void setDateReservation(Date dateReservation) { this.dateReservation = dateReservation; }

    public Date getDateRdv() { return dateRdv; }
    public void setDateRdv(Date dateRdv) { this.dateRdv = dateRdv; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Acte getActe() { return acte; }
    public void setActe(Acte acte) { this.acte = acte; }

    public CreneauReserve getCreneau() { return creneau; }
    public void setCreneau(CreneauReserve creneau) { this.creneau = creneau; }
    

    public void valider() {
        if (etat.equals("annulé")) {
            throw new IllegalStateException("Impossible de valider un rendez-vous annulé");
        }
        etat = "validé";
    }

    public void annuler() {
        etat = "annulé";
    }

    public String genererFiche() {
           DateTimeFormatter jour = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           return "CABINET DENTAIRE\n"
                   + "Fiche de rendez-vous\n"
                   + "------------------------------\n"
                   + "Patient      : " + patient.getPrenom() + " " + patient.getNom() + "\n"
                   + "Dentiste     : Dr " + creneau.getDentiste().getPrenom() + " "
                           + creneau.getDentiste().getNom() + "\n"
                   + "Acte         : " + acte.getLibelle() + "\n"
                   + "Date         : " + creneau.getDate().format(jour) + "\n"
                   + "Heure        : " + creneau.getHeureDebut() + " - " + creneau.getHeureFin() + "\n"
                   + "Duree prevue : " + acte.getDuree() + " minutes\n"
                   + String.format(Locale.FRENCH, "Tarif        : %.2f MAD\n", acte.getTarif())
                   + "Etat         : " + etat + "\n";
    }

    public void exporterPDF(File fichier) throws IOException {
           PdfExporter.exporter(this, fichier);
    }

    

    @Override
    public String toString() {
        return "RendezVous " + idRdv + " (" + etat + ")";
    }
}