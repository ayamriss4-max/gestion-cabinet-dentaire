package app.cabinet.persistance;

import app.cabinet.modele.Dentiste;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CabinetDao {

    public static void sauvegarderDentistes(List<Dentiste> dentistes, String nomFichier) {
        Path chemin = Paths.get(nomFichier);
        try (BufferedWriter writer = Files.newBufferedWriter(chemin, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Dentiste d : dentistes) {
                String ligne = d.getIdDentiste() + ";" + d.getNom() + ";" + d.getPrenom() + ";" + d.getSpecialite();
                writer.write(ligne);
                writer.newLine();
            }
            writer.flush();
            System.out.println("Sauvegarde des dentistes réussie dans : " + chemin.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static List<Dentiste> lireDentistes() {
        List<Dentiste> dentistes = new ArrayList<>();
        Path chemin = Paths.get("dentistes.txt");
        
        if (Files.exists(chemin)) {
            try (BufferedReader reader = Files.newBufferedReader(chemin)) {
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String nom = parts[1];
                        String prenom = parts[2];
                        String specialite = parts[3];
                        
                        Dentiste d = new Dentiste(id, nom, prenom, specialite);
                        dentistes.add(d);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture : " + e.getMessage());
            }
        }
        return dentistes;
    }
}