import app.cabinet.modele.Acte;
import app.cabinet.modele.Creneau;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CabinetMetierTest {

    @Test
    public void testDureeCreneauSuffisante() {
        Creneau creneau = new Creneau(1, "2026-10-21 10:00", 30, 1);
        assertTrue(creneau.getDureeMinutes() >= 15, "Le créneau doit durer au moins 15 minutes");
    }

    @Test
    public void testCreationActeTarif() {
        Acte acte = new Acte(1, "Détartrage", 250.0);
        assertEquals("Détartrage", acte.getLibelle());
        assertEquals(250.0, acte.getTarif(), 0.01);
    }
}
