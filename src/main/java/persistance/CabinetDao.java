package persistance;

import modele.Dentiste;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
}
