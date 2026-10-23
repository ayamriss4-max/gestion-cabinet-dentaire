package app.cabinet.vue; // Assurez-vous que c'est le bon package

import app.cabinet.modele.RendezVous;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PdfExporter {

    public static final String NOM_CABINET = "Cabinet dentaire";

    public static void exporter(RendezVous r, File fichier) throws IOException {
        DateTimeFormatter jour = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float x = 70;
                float y = 780;

                texte(cs, PDType1Font.HELVETICA_BOLD, 20, x, y, NOM_CABINET);
                y -= 28;
                texte(cs, PDType1Font.HELVETICA_BOLD, 14, x, y, "Fiche de rendez-vous");
                y -= 12;
                cs.moveTo(x, y);
                cs.lineTo(525, y);
                cs.stroke();
                y -= 34;

                y = champ(cs, x, y, "Patient",
                        r.getPatient().getPrenom() + " " + r.getPatient().getNom());
                y = champ(cs, x, y, "Dentiste",
                        "Dr " + r.getCreneau().getDentiste().getPrenom() + " "
                                + r.getCreneau().getDentiste().getNom());
                y = champ(cs, x, y, "Acte", r.getActe().getLibelle());
                y = champ(cs, x, y, "Date", r.getCreneau().getDate().format(jour));
                y = champ(cs, x, y, "Heure", r.getCreneau().getHeureDebut()
                        + " - " + r.getCreneau().getHeureFin());
                y = champ(cs, x, y, "Duree prevue", r.getActe().getDuree() + " minutes");
                y = champ(cs, x, y, "Tarif",
                        String.format(Locale.FRENCH, "%.2f MAD", r.getActe().getTarif()));
                champ(cs, x, y, "Etat", r.getEtat());

                texte(cs, PDType1Font.HELVETICA_OBLIQUE, 9, x, 60,
                        "Document edite le " + LocalDate.now().format(jour));
            }
            doc.save(fichier);
        }
    }

    private static float champ(PDPageContentStream cs, float x, float y,
                               String libelle, String valeur) throws IOException {
        texte(cs, PDType1Font.HELVETICA_BOLD, 12, x, y, libelle + " :");
        texte(cs, PDType1Font.HELVETICA, 12, x + 140, y, valeur);
        return y - 26;
    }

    private static void texte(PDPageContentStream cs, PDFont police, float taille,
                              float x, float y, String contenu) throws IOException {
        cs.beginText();
        cs.setFont(police, taille);
        cs.newLineAtOffset(x, y);
        cs.showText(contenu);
        cs.endText();
    }
}