package sample;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * Class TextFieldDisplay
 * @author  Monika Regułą
 * @version 1.0
 */

public class TextFieldDisplay implements Observer,Display {
    /**
     * Represents arraylist of temperature values
     */
    private ArrayList<Double> temps = new ArrayList<>();
    /**
     * Represents arraylist of humidity values
     */
    private ArrayList<Double> hum = new ArrayList<>();
    /**
     * Represents arraylist of pressure values
     */
    private ArrayList<Double> press = new ArrayList<>();
    /**
     * Represents TextField with number of measurements in JavaFX
     */
    private TextField NumberTxt;
    /**
     * Represents TextField with max temperature in JavaFX
     */
    private TextField TMaxTxt;
    /**
     * Represents TextField  with max humidity in JavaFX
     */
    private TextField HMaxTxt;
    /**
     * Represents TextField with max pressure in JavaFX
     */
    private TextField PMaxTxt;
    /**
     * Represents TextField with standard deviation of temperature in JavaFX
     */
    private TextField TStdTxt;
    /**
     * Represents TextField with standard deviation of humidity in JavaFX
     */
    private TextField HStdTxt;
    /**
     * Represents TextField with standard deviation of pressure in JavaFX
     */
    private TextField PStdTxt;
    /**
     * Represents values that are going to be updated in TextFields in JavaFX
     */
    private double[] data = new double[7];

    /**
     * Makes an Object of TextFiledDisplay class with parameters that are calculated
     * @param numberTxt number of measurements
     * @param TMaxTxt max temperature
     * @param HMaxTxt max humidity
     * @param PMaxTxt max pressure
     * @param TStdTxt standard deviation of temperature
     * @param HStdTxt standard deviation of humidity
     * @param PStdTxt standars deviation pressure
     */
    public TextFieldDisplay(TextField numberTxt, TextField TMaxTxt, TextField HMaxTxt, TextField PMaxTxt, TextField TStdTxt, TextField HStdTxt, TextField PStdTxt) {
        this.NumberTxt = numberTxt;
        this.TMaxTxt = TMaxTxt;
        this.HMaxTxt = HMaxTxt;
        this.PMaxTxt = PMaxTxt;
        this.TStdTxt = TStdTxt;
        this.HStdTxt = HStdTxt;
        this.PStdTxt = PStdTxt;
    }

    /**
     * Method displays
     */
    @Override
    public void display() {
        Platform.runLater(() -> {
            count();
            NumberTxt.setText(String.valueOf(data[0]));
            TMaxTxt.setText(String.valueOf(data[1]));
            HMaxTxt.setText(String.valueOf(data[2]));
            PMaxTxt .setText(String.valueOf(data[3]));
            TStdTxt.setText(String.valueOf(data[4]));
            HStdTxt.setText(String.valueOf(data[5]));
            PStdTxt.setText(String.valueOf(data[6]));
        });
    }

    /**
     * Method calculates statistics parameters as standard deviation for : humidity,pressure and temperature.
     * It calculates maximal values of:temperature, humidity and pressure as well.
     */
    private void count() {
        double stdT1 = 0;
        double stdH1 = 0;
        double stdP1 = 0;
        double maxT = temps.get(0);
        double maxH = hum.get(0);
        double maxP = press.get(0);
        double sumT = 0;
        double sumH = 0;
        double sumP = 0;
        double meanT;
        double meanH;
        double meanP;
        int a = 0;
        int n = temps.size();

        for (int i = 0; i < n - 1; i++) {

            if (temps.get(i) > maxT) {
                maxT = temps.get(i);
            }
            if (hum.get(i) > maxH) {
                maxH = hum.get(i);
            }
            if (press.get(i) > maxP) {
                maxP = press.get(i);
            }

            //srednia
            sumT += temps.get(i);
            sumH += hum.get(i);
            sumP += press.get(i);

        }
        meanT = sumT / n;
        meanH = sumH / n;
        meanP = sumP / n;
        for (int i=0;i<n;i++){
            stdT1 += Math.pow((temps.get(i) - meanT), 2.0);
            stdH1 += Math.pow((hum.get(i) - meanH), 2.0);
            stdP1 += Math.pow((press.get(i) - meanP), 2.0);
        }

        double stdT = Math.sqrt(stdT1 / (n));
        System.out.println("stdT: "+stdT);
        double stdH = Math.sqrt(stdH1 / (n));
        double stdP = Math.sqrt(stdP1 / (n));
       stdT = Math.round(stdT * 100.0) / 100.0;
        //System.out.println("std po round: "+stdT);
        //stdH = Math.round(stdH * 100.0) / 100.0;
        //stdP = Math.round(stdP * 100.0) / 100.0;
        data = new double[]{n, maxT, maxH, maxP, stdT, stdH, stdP};

    }

    /**
     * Method updates weatherBase's parameters and display
     * @param weatherBase weatherBase
     */
    @Override
    public void update(WeatherBase weatherBase) {
        if (weatherBase.getPressure() != 0) {
            temps.add(weatherBase.getTemp());
            hum.add(weatherBase.getHumidity());
            press.add(weatherBase.getPressure());
        }
        display();
    }
}
