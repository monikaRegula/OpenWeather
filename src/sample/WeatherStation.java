package sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;


/**
 * Class WeatherStation
 * @author Monika Reguła
 * @version 1.0
 */
public class WeatherStation implements Runnable,Observable{
    /**
     * Represents result Thread
     */
    private Thread worker;
    /**
     * Represents state of Thread
     */
    protected volatile boolean isRunning = false;
    /**
     * Represents value of interval
     */
    private int interval=10000;
    /**
     * Represents amount of measurement
     */
    private int n=0;
    /**
     * Represents list of Observers
     */
    private volatile ArrayList<Observer> weatherUpdates = new ArrayList<>();

    /**
     * Represents result received from OpenWeather
      */
    private String result;
    /**
     * Represents name of the city
     */
    private String city;
    /**
     * Represents parameters of the weather
     */
    private WeatherBase weatherBase = new WeatherBase();


    /**
     * Creates object WeatherStation with 'city' as a parameter
     * @param city
     */
    public WeatherStation(String city) {
        this.city = city;
    }

    /**
     * Sets interval of the Thread
     * @param interval
     */
    public void setInterval(int interval) { this.interval = interval; }

    /**This method get information about weather from website openWeather as a json file
     * and returns it as String result
     *
     * @return result
     */
    private String getWeather() {
        try {

            StringBuilder link = new StringBuilder();
            link.append("http://api.openweathermap.org/data/2.5/weather?q=");
            link.append(city);
            link.append("&units=metric");
            link.append("&APPID=8d4fac6d7e212a6607eb11da058d1534");


           //Tworzę StringBuffer, który zbiera po linijce pilk jsona otrzymany z OpenWeather
            StringBuffer response = new StringBuffer();
            String url = link.toString();
            URL obj = new URL(url); // Tworzę obiekt klasy URL jego parameterm jest link w postaci Stringa

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection(); // wykonuje połączenie z serwerem
            connection.setRequestMethod("GET"); //używam metody get- pobierającej
            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));//tworzę obiket klasy BufferReader
            //który wczytuje kolejne linie pliku
            String line; //to zmiena potrzebna do warunku, który sprawdza czy kolejna linia nie jest przypadkiem pusta

            while ((line = in.readLine()) != null) { //pętla dodaje do StringBuffera linijki odczytywanego pliku jsona za pomocą metody append
                response.append(line);
            }
            in.close(); //zamykam nawiązywanie połączenia
            result = response.toString();//wszystkie odczytane linijki przez BufferReadera są zapisane do pola result klasy WeatherStation

            //obsługa wyjątków
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There is no city as this");
        } finally {
            return result;
        }

    }

    /**
     * Method maps received jsonfile saved as a String ' result' ,
     * then sets specific parameters in Object WeatherBase
     */
    private void mapWeather() {
        //tworzę GsonBuildera741
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String weather = getWeather(); //zmienna będaca zmienna zwrotną metody getWeather
        Map m = gson.fromJson(weather, Map.class); //tworze mapę obiektu
        String output = m.get("main").toString(); //tworzę Stringa z pożądaną wartością pola obiektu
        output = output.replaceAll("[^\\d.\\s\\-]", ""); //usuwam z pola wszystkie kropki,spacje i nieliczby
        String[] parameters = output.split(" "); //rozdzielenie Stringa na tablice Stringów , gdzie każdy rodzielony był spacją

        //obiekt weatherBase otrzymuje wartości zapisane jako jego pola
        weatherBase = new WeatherBase();
        weatherBase.setTemp(Double.parseDouble(parameters[0]));
        weatherBase.setPressure(Double.parseDouble(parameters[1]));
        weatherBase.setHumidity(Double.parseDouble(parameters[2]));
        weatherBase.setTime(LocalTime.now());
        weatherBase.setN(n);
        n++;
    }

    /**
     * When Thread is started then methods:mapWeather and updateObservers are
     * started. Then Thread goes to sleep for interval period time
     */
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {

            try {
                mapWeather();//mapuję jsona
                updateObservers(weatherBase);//update obiektu
                Thread.sleep(interval);// program czeka przez interwał
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Something went wrong");
            }
        }
    }

    /**
     * Method creates and starts Thread
     */
    public void start() {
        worker= new Thread(this, " Clock thread");
        worker.start();
    }

    /**
     * Method stops Thread
     */
    public void stop() {
        isRunning = false;
    }//metoda zatrzymuje program

    /**
     * Method interrupts Thread
     */
    public void interrupt() {//metoda do przycisku PAUSE
        isRunning = false;
        worker.interrupt();
    }

    /**
     * Method adds an Observer to ObserverList if list does not contain an Observer
     * @param observer Observer
     */
    @Override
    public void addObserver(Observer observer) {
        //metoda dodaje obserwatora do listy tylko jęsli jeszcze go na niej nie ma
        if (!weatherUpdates.contains(observer))
            weatherUpdates.add(observer);
    }

    /**Method removes an Observer form ObserverList
     *
     * @param observer Observer
     */
    @Override
    public void removeObserver(Observer observer) {
        //metoda usuwa obserwatora jeśli istnieje on na liście
        if (weatherUpdates.contains(observer)) {
            weatherUpdates.remove(observer);
        } else {//inaczej wyrzuca wyjątek
            throw new IllegalArgumentException("No such observer");
        }
    }


    /**
     * Method updates Observer with the current weather's parameters
     * @param weatherBase Current weatherBase
     */
    @Override
    public void updateObservers(WeatherBase weatherBase) {
        Platform.runLater(() -> {
            //dla obserwatorów uaktualnia się pole weatherBse po każdym ponownym odpalaniu się programu
            for (Observer observers : weatherUpdates) {
                observers.update(weatherBase);
            }
        });
    }
}
