package modele;

import java.time.LocalDate;

public class RendezVous {

    public static final String ATTENTE = "en attente";
    public static final String VALIDE = "validé";
    public static final String ANNULE = "annulé";

    private int idRdv;
    private LocalDate dateReservation;
    private LocalDate dateRdv;
    private String etat;
    private Patient patient;
    private Acte acte;
    private Creneau creneau;

    public RendezVous(int idRdv, LocalDate dateReservation, LocalDate dateRdv, String etat,
                      Patient patient, Acte acte, Creneau creneau) {
        this.idRdv = idRdv;
        this.dateReservation = dateReservation;
        this.dateRdv = dateRdv;
        this.etat = etat;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
    }

    public int getIdRdv() { return idRdv; }
    public void setIdRdv(int idRdv) { this.idRdv = idRdv; }

    public LocalDate getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDate dateReservation) { this.dateReservation = dateReservation; }

    public LocalDate getDateRdv() { return dateRdv; }
    public void setDateRdv(LocalDate dateRdv) { this.dateRdv = dateRdv; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Acte getActe() { return acte; }
    public void setActe(Acte acte) { this.acte = acte; }

    public Creneau getCreneau() { return creneau; }
    public void setCreneau(Creneau creneau) { this.creneau = creneau; }

    public void valider() { this.etat = VALIDE; }
    public void annuler() { this.etat = ANNULE; }
}