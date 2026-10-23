package app.cabinet.modele;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class RendezVous implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EN_ATTENTE = "en attente";
    public static final String VALIDE = "validé";
    public static final String ANNULE = "annulé";

    private int idRdv;
    private Date dateReservation;
    private Date dateRdv;
    private String etat;
    private Patient patient;
    private Acte acte;
    private CreneauReserve creneau;

    // 1. Constructeur principal (avec Date)
    public RendezVous(int idRdv, Date dateRdv, Patient patient, Acte acte, CreneauReserve creneau) {
        this.idRdv = idRdv;
        this.dateReservation = new Date();   
        this.dateRdv = dateRdv;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
        this.etat = EN_ATTENTE;              
        if (this.creneau != null) {
            this.creneau.setRendezVous(this);         
        }
    }

    // 2. Constructeur pour la persistance/chargement (avec LocalDate, utilisé par Donnee.java)
    public RendezVous(int idRdv, LocalDate dateReservation, LocalDate dateRdv, String etat, 
                      Patient patient, Acte acte, CreneauReserve creneau) {
        this.idRdv = idRdv;
        this.dateReservation = Date.from(dateReservation.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.dateRdv = Date.from(dateRdv.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.etat = etat;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
        if (this.creneau != null) {
            this.creneau.setRendezVous(this);
        }
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
        if (ANNULE.equals(etat)) {
            throw new IllegalStateException("Impossible de valider un rendez-vous annulé");
        }
        etat = VALIDE;
    }

    public void annuler() {
        etat = ANNULE;
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

    @Override
    public String toString() {
        return "RendezVous " + idRdv + " (" + etat + ")";
    }
}