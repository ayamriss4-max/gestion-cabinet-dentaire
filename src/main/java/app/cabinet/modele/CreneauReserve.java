package app.cabinet.modele; // Correction du package

public class CreneauReserve extends Creneau {
    private RendezVous rendezVous;

    public CreneauReserve(Creneau creneau) {
        // Appel du constructeur parent avec les bons types
        super(creneau.getId(), creneau.getDate(), creneau.getHeureDebut(), 
              creneau.getHeureFin(), creneau.getType(), creneau.getDentiste());
        
        if (creneau.estPasse()) {
            throw new IllegalStateException("Impossible de réserver un créneau passé");
        }
        if (!creneau.estLibre()) {
            throw new IllegalStateException("Ce créneau est déjà réservé");
        }
    }

    @Override
    public boolean estLibre() {
        // Un créneau réservé est libre si le RDV est annulé
        return rendezVous != null && RendezVous.ANNULE.equals(rendezVous.getEtat());
    }

    public RendezVous getRendezVous() { return rendezVous; }
    public void setRendezVous(RendezVous rendezVous) { this.rendezVous = rendezVous; }
}