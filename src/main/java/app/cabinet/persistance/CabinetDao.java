package app.cabinet.persistance;

import app.cabinet.modele.Acte;
import app.cabinet.modele.Creneau;
import app.cabinet.modele.Dentiste;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CabinetDao {

    
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
                    liste.add(new Acte(id, libelle, tarif));
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
}