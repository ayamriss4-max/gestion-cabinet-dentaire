package app.cabinet.modele;

import java.time.LocalDate;

public class Patient extends Utilisateur {
    
    private String cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private LocalDate dateNaissance;

    public Patient(String login, String pwdHache, String cin, String nom, String prenom, String adresse, String telephone, LocalDate dateNaissance) {
        super(login, pwdHache);
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
    }
    public Patient(String nom, String prenom) {
      
        super(nom.toLowerCase() + "." + prenom.toLowerCase(), ""); 
        
        this.cin = "00000000"; 
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = "";
        this.telephone = "";
        this.dateNaissance = LocalDate.now(); 
    }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    @Override
    public String getRole() { 
        return "PATIENT"; 
    }
}