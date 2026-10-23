package app.cabinet.modele;

import java.util.ArrayList;
import java.util.List;

public class Administrateur extends Utilisateur {

    public Administrateur(String login, String pwdHache) {
        super(login, pwdHache);
    }

    @Override
    public String getRole() { 
        return "ADMIN"; 
    }

    // Les méthodes gerer...() reçoivent la liste à gérer et la renvoient :
    // les fenêtres CRUD ajouteront, modifieront et supprimeront dans cette liste.
    public List<Patient> gererPatients(List<Utilisateur> utilisateurs) {
        List<Patient> patients = new ArrayList<>();
        for (Utilisateur u : utilisateurs) {
            if (u instanceof Patient) {
                patients.add((Patient) u);
            }
        }
        return patients;
    }

    public List<Dentiste> gererDentistes(List<Dentiste> dentistes) { return dentistes; }
    public List<Acte> gererActes(List<Acte> actes) { return actes; }
    public List<Creneau> gererCreneaux(List<Creneau> creneaux) { return creneaux; }

    public void validerRdv(RendezVous rdv) { rdv.valider(); }
    public void annulerRdv(RendezVous rdv) { rdv.annuler(); }
}