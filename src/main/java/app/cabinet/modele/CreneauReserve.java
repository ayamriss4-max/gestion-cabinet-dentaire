package app.cabinet.modele;

public class CreneauReserve extends Creneau {

    private RendezVous rendezVous;

   
    public CreneauReserve(Creneau creneau) {
        super(creneau.getIdCreneau(), creneau.getDate(), creneau.getHeureDebut(),
              creneau.getHeureFin(), creneau.getType(), creneau.getDentiste());
        if (!creneau.estLibre()) {
            throw new IllegalStateException("Ce créneau est déjà réservé");
        }
        
        if (creneau.estPasse()) {
            throw new IllegalStateException("Impossible de réserver un créneau passé");
        }

        
    }

    @Override
    public boolean estLibre() {
        return rendezVous != null
            && RendezVous.ANNULE.equals(rendezVous.getEtat());
}
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
    }
    
    
}