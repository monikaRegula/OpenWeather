package sample;

import java.time.LocalTime;

/**
 * Class WeatherBase
 * @author Monika Reguła
 * @version 1.0
 */
public class WeatherBase {
    /**
     * Represents value of temperature
     */
    private double temp;
    /**
     * Represents value of pressure
     */
    private double pressure;
    /**
     * Represents value of humidity
     */
    private double humidity;
    /**
     * Represents value of maximum temperature
     */
    private double tempMax;
    /**
     * Represents value of minimum temperature
     */
    private double tempMin;
    /**
     * Represents amount of measurement
     */
    private int n;
    /**
     * Represents current time
     */
    private LocalTime time;

    /**
     * Gets actual temperature
     * @return temperature
     */
    public double getTemp() { return temp; }

    /**
     * Sets actual temperature
     */
    public void setTemp(double temp) { this.temp = temp; }

    /**
     * Gets actual pressure
     * @return pressure
     */
    public double getPressure() { return pressure; }

    /**
     * Sets actual pressure
     * @param pressure pressure
     */
    public void setPressure(double pressure) { this.pressure = pressure; }

    /**
     * Gets actual humidity
     * @return humidity
     */
    public double getHumidity() { return humidity; }

    /**
     * Sets actual humidity
     * @param humidity humidity
     */
    public void setHumidity(double humidity) { this.humidity = humidity; }

    /**
     * Gets actual value of max temperature
     * @return tempMax
     */
    public double getTempMax() { return tempMax; }

    /**
     * Sets value of max temperature
     * @param tempMax max temperature
     */
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }

    /**
     * Gets actual value of minimum temperature
     * @return tempMin
     */
    public double getTempMin() { return tempMin; }

    /**
     * Sets actual value of min temperature
     * @param tempMin min temperature
     */
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }

    /**
     * Gets amount of measurements
     * @return n
     */
    public int getN() { return n; }

    /**
     * Sets amount of measurements
     * @param n n
     */
    public void setN(int n) { this.n = n; }

    /**
     * Gets time
     * @return time
     */
    public LocalTime getTime() { return time; }

    /**
     * Sets time
     * @param time time
     */
    public void setTime(LocalTime time) { this.time = time; }

    /**
     * Methods shows information in IntelliJ Console
     * @return amount of measuremnts,actual temperature,pressure,humidity
     */
    @Override
    public String toString() {
        return n + " : " + temp + " : " + pressure + " : " + humidity;
    }
}
