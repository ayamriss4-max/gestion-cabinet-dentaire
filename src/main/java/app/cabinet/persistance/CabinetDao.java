package app.cabinet.persistance;

import app.cabinet.modele.*;
import app.cabinet.modele.RendezVous;
import app.cabinet.persistance.RdvDao;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CabinetDao {

	private static CabinetDao instance;

    private final RdvDao rdvDao = new RdvDao();
    private final ObservableList<RendezVous> rdvs =
            FXCollections.observableArrayList();
    private final IntegerProperty modifications = new SimpleIntegerProperty(0);        

    
    public static List<Dentiste> lireDentistes() {
        List<Dentiste> liste = new ArrayList<>();
        File file = new File("dentistes.txt");
        if (!file.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 4) {
                    int id = Integer.parseInt(p[0].trim());
                    String nom = p[1].trim();
                    String prenom = p[2].trim();
                    String specialite = p[3].trim();
                    liste.add(new Dentiste(id, nom, prenom, specialite));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void sauvegarderDentistes(List<Dentiste> liste, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Dentiste d : liste) {
                bw.write(d.getId() + ";" + d.getNom() + ";" + d.getPrenom() + ";" + d.getSpecialite());
                bw.newLine();
            }
            System.out.println("Sauvegarde des dentistes réussie dans : " + new File(filename).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Creneau> lireCreneaux() {
        List<Creneau> liste = new ArrayList<>();
        File file = new File("creneaux.txt");
        
        if (!file.exists()) {
            System.out.println("⚠️ Fichier creneaux.txt introuvable à l'emplacement : " + file.getAbsolutePath());
            return liste;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(";");
                if (p.length == 4) {
                    int id = Integer.parseInt(p[0].trim());
                    String dateHeure = p[1].trim();
                    int duree = Integer.parseInt(p[2].trim());
                    int dentisteId = Integer.parseInt(p[3].trim());
                    liste.add(new Creneau(id, dateHeure, duree, dentisteId));
                }
            }
            System.out.println("✅ Nombre de créneaux chargés : " + liste.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void sauvegarderCreneaux(List<Creneau> liste, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Creneau c : liste) {
                bw.write(c.getId() + ";" + c.getDateHeure() + ";" + c.getDureeMinutes() + ";" + c.getDentisteId());
                bw.newLine();
            }
            System.out.println("Sauvegarde des créneaux réussie dans : " + new File(filename).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Acte> lireActes() {
        List<Acte> liste = new ArrayList<>();
        File file = new File("actes.txt");
        
        if (!file.exists()) {
            System.out.println("⚠️ Fichier actes.txt introuvable à l'emplacement : " + file.getAbsolutePath());
            return liste;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(";");
                if (p.length == 3) {
                    int id = Integer.parseInt(p[0].trim());
                    String libelle = p[1].trim();
                    double tarif = Double.parseDouble(p[2].trim());
                    liste.add(new Acte(id, libelle, tarif,duree));
                }
            }
            System.out.println("✅ Nombre d'actes chargés : " + liste.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public static void sauvegarderActes(List<Acte> liste, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Acte a : liste) {
                bw.write(a.getId() + ";" + a.getLibelle() + ";" + a.getTarif());
                bw.newLine();
            }
            System.out.println("Sauvegarde des actes réussie dans : " + new File(filename).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private CabinetDao() {
        chargerTout();
    }

    public static CabinetDao getInstance() {
        if (instance == null) {
            instance = new CabinetDao();
        }
        return instance;
    }

    public ObservableList<RendezVous> getRdvs() { return rdvs; }

    public void ajouterRdv(RendezVous r) {
        rdvs.add(r);
        sauvegarderTout();
    }
    public void supprimerRdv(RendezVous r) {
        rdvs.remove(r);
        sauvegarderTout();
    }

    private void chargerTout() {
        rdvs.setAll(rdvDao.charger());
    }

    public IntegerProperty modificationsProperty() {
        return modifications;
    }

    public void sauvegarderTout() {
        try {
            rdvDao.sauvegarder(new ArrayList<>(rdvs));   
            modifications.set(modifications.get() + 1);  
        } catch (RuntimeException e) {
            System.err.println("Erreur de sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}