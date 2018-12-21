package sample;

/**
 * Interface Observer
 * @author Monika Reguła
 * @version 1.0
 */
public interface Observer {
    /**
     * Method gets updated data from observable
     * @param weatherBase weatherBase
     */
    void update(WeatherBase weatherBase);
}
