package sample;

import javafx.collections.ObservableList;

/**
 * Class WeatherUpdate
 * @author Monika Reguła
 * @version 1.0
 */
public class WeatherUpdate implements Observer {
    /**
     * List of observers that are WeatherBase's atributes
     */
    private ObservableList<WeatherBase> data;

    /**
     * Makes object of the WeatherUpdate class with parameter data
     * @param data data
     */
    public WeatherUpdate(ObservableList<WeatherBase> data) {
        this.data = data;
    }

    /**
     * Method is overrided from interface Observer. It makes parameters of weatherBase updated
     * @param weatherBase weatherBase
     */
    @Override
    public void update(WeatherBase weatherBase) {
        System.out.println(weatherBase);
        data.add(weatherBase);
    }
}
