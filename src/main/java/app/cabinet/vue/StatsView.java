package app.cabinet.vue;

import app.cabinet.modele.RendezVous;
import app.cabinet.persistance.CabinetDao;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Map;
import java.util.TreeMap;

public class StatsView extends BorderPane {

    private final CategoryAxis axeX = new CategoryAxis();
    private final NumberAxis axeY = new NumberAxis();
    private final BarChart<String, Number> graphique = new BarChart<>(axeX, axeY);
    private final CheckBox seulementValides = new CheckBox("Rendez-vous validés seulement");

    public StatsView() {
        axeX.setLabel("Dentiste");
        axeY.setLabel("Nombre de rendez-vous");
        axeY.setMinorTickVisible(false);
        graphique.setTitle("Rendez-vous par dentiste");
        graphique.setLegendVisible(false);

        seulementValides.setOnAction(e -> mettreAJour());
        CabinetDao.getInstance().modificationsProperty()
                .addListener((obs, ancien, nouveau) -> mettreAJour());

        HBox barre = new HBox(10, seulementValides);
        barre.setPadding(new Insets(10));
        setTop(barre);
        setCenter(graphique);

        mettreAJour();
    }

    private void mettreAJour() {
        Map<String, Integer> compte = new TreeMap<>();
        for (RendezVous r : CabinetDao.getInstance().getRdvs()) {
            if (seulementValides.isSelected() && !RendezVous.VALIDE.equals(r.getEtat())) {
                continue;
            }
            String dentiste = String.valueOf(r.getCreneau().getDentiste());
            compte.merge(dentiste, 1, Integer::sum);
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (Map.Entry<String, Integer> e : compte.entrySet()) {
            serie.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }
        graphique.getData().setAll(serie);
    }
}