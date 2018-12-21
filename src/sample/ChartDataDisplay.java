package sample;
/**
 Class ChartDataDisplay updates series needed to show chart
 *@author Monika Reguła
 *@version 1.0
 */
import javafx.application.Platform;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;


public class ChartDataDisplay implements Observer,Display {
    /**
     * Represents chart
     */
    private ScatterChart<Number, Number> chart;
    /**
     * Represents chart temperature series
     */
    private XYChart.Series<Number, Number> Tseries = new XYChart.Series<>();
    /**
     * Represents chart humidity series
     */
    private XYChart.Series<Number, Number> Hseries = new XYChart.Series<>();
    /**
     * Represents chart pressure series
     */
    private XYChart.Series<Number, Number> Pseries = new XYChart.Series<>();
    /**
     * Represents y Axis for chart
     */
    private Axis yAxis;
    /**
     * Represents component Choicebox
     */
    private ChoiceBox choice;

    /**
     * Constructor makes an Object class ChartDataDisplay
     * @param chart chart
     * @param yAxis y Axis
     * @param choice choice (type of chart)
     */
    public ChartDataDisplay(ScatterChart<Number, Number> chart, Axis yAxis, ChoiceBox choice) {
        this.chart = chart;
        this.yAxis = yAxis;
        this.choice = choice;
    }


    /**
     * Method display title of the chart by choice parameter form choicebox
     */
    @Override
    public void display() {
        //ustawiam nazwę dla osi y:
        Tseries.setName("Temperature");
        Hseries.setName("Humidity");
        Pseries.setName("Pressure");
        chart.setAnimated(false);
        chart.getData().removeAll(chart.getData());
       /* Node fill = Tseries.getNode().lookup(".chart-series-area-fill"); // only for AreaChart
       // Node line = Tseries.getNode().lookup(".chart-series-area-line");

        Color color = Color.RED; // or any other color

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        //fill.setStyle("-fx-fill: rgba(" + rgb + ", 0.15);");
       // line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");*/
        //w zależności od klikniętego wyboru w CHoicebox jako dane dla osi y wybiera co innego:
        if (choice.getValue() == "Temperature") {
            chart.getData().addAll(Tseries);
            yAxis.setLabel("Temperature");
        } else if (choice.getValue() == "Humidity") {
            chart.getData().addAll(Hseries);
            yAxis.setLabel("Humidity");
        } else {
            chart.getData().addAll(Pseries);
            yAxis.setLabel("Pressure");
        }

    }

    /**
     * Method updates data that will be shown in chart and then display them
     * @param weatherBase weatherBase
     */
    @Override
    public void update(WeatherBase weatherBase) {
        Platform.runLater(() -> {
            if (weatherBase.getHumidity() != 0) {
                //przypisuje pola klasy weatherBase do serii danych wykresow
                Tseries.getData().add(new XYChart.Data<>(weatherBase.getN(), weatherBase.getTemp()));
                Hseries.getData().add(new XYChart.Data<>(weatherBase.getN(), weatherBase.getHumidity()));
                Pseries.getData().add(new XYChart.Data<>(weatherBase.getN(), weatherBase.getPressure()));
            }
        });
        display();//wyświetlam patrz metoda wyżej ^^
    }
}
