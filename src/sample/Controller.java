package sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.*;
import java.util.Arrays;


/**
 * Class Controller
 * @Monika Reguła
 * @version 1.0
 */
public class Controller {
    /**
     * Represents Object of class weatherStation
     */
    private WeatherStation basic;
    /**
     * Represents Observable List of Observers :
     */
    private ObservableList<String> choices =
            FXCollections.observableArrayList(
                    "Temperature",
                    "Humidity",
                    "Pressure"
            );

    private final ObservableList<WeatherBase> data = FXCollections.observableArrayList(
            new WeatherBase());

    /**
     * Represents Object of WeatherBase Class
     */
    private WeatherBase weather = new WeatherBase();

    /**
     * Default Constructor of Controller Class
     * It makes an Object of Controller Class
     */
    public Controller() {
    }

    /**
     * Represents TextField with city name in JavaFX
     */
    @FXML
    private TextField txtCity;
    /**
     * Represents button Start
     */
    @FXML
    private Button btnStart;
    /**
     * Represents TextField with time step in JavaFX
     */
    @FXML
    private TextField txtInterval;
    /**
     * Represents button Pause
     */
    @FXML
    private Button btnPause;
    /**
     * Represents button Unpause
     */
    @FXML
    private Button btnUnpasue;
    /**
     * Represents table in JavaFX
     */
    @FXML
    private TableView<WeatherBase> tabview;
    /**
     * Represents single column of table in JavaFX
     */
    @FXML
    private TableColumn<WeatherBase, String> TableColumnLp;
    /**
     * Represents single column of table in JavaFX
     */
    @FXML
    private TableColumn<WeatherBase, String> TableColumnTime;
    /**
     * Represents single column of table in JavaFX
     */
    @FXML
    private TableColumn<WeatherBase, String> TableColumnTemp;
    /**
     * Represents single column of table in JavaFX
     */
    @FXML
    private TableColumn<WeatherBase, String> TableColumnHum;
    /**
     * Represents single column of table in JavaFX
     */
    @FXML
    private TableColumn<WeatherBase, String> TableColumnPress;
    /**
     * Represents chart in JavaFX
     */
    @FXML
    private ScatterChart<Number, Number> chart1;
    /**
     * Represents of x Axis
     */
    @FXML
    private NumberAxis xAxis;
    /**
     * Represents of y Axis
     */
    @FXML
    private NumberAxis yAxis;
    /**
     * Represents of ChoiceBox in JavaFX
     */
    @FXML
    private ChoiceBox<String> choiceOfCHart;
    /**
     * Represents of TextField with file name in JavaFX
     */
    @FXML
    private TextField txtFileName;
    /**
     * Represents button Stop and Save to file
     */
    @FXML
    private Button btnStopandSave;
    /**
     * Represents Textfield with standard deviation of temperature in JavaFX
     */
    @FXML
    private TextField txtSDtemp;
    /**
     * Represents Textfield with standard deviation of humidity in JavaFX
     */
    @FXML
    private TextField txtSDhum;
    /**
     * Represents Textfield with standard deviation of pressure in JavaFX
     */
    @FXML
    private TextField txtSDpress;
    /**
     * Represents TextField  with max temperature in JavaFX
     */
    @FXML
    private TextField txtMaxTemp;
    /**
     * Represents TextField  with max humidity in JavaFX
     */
    @FXML
    private TextField txtMaxHum;
    /**
     * Represents TextField  with max pressure in JavaFX
     */
    @FXML
    private TextField txtMaxPress;
    /**
     * Represents TextField  with number of measuremnts in JavaFX
     */
    @FXML
    private TextField txtNumber;
    /**
     * It shows as an Exception if user writes too small time step (interval)
     */
    @FXML
    private Text txtIntervalException;

    @FXML
    private Text txtCityException;

    @FXML
    private TextField txtFileName1;


    @FXML
    private Button btnReadFile;


    @FXML
    private Pane Pane;

    @FXML
    private TextField txtFIleName2;


    /**
     * Method is activated when button Start is pressed. It gets values written by user in GUI and uses them to
     * get data from website and process. Processing = making chart of temperature/humidity/pressure and displaying calculated statistics values
     * @param event event
     */
    @FXML
    void startPressed(ActionEvent event) {
       Pane.setStyle("-fx-background-color: fuchsia");
        //przygotowanie przycisków
        //po wciśnięciu START można kliknąc UNPAUSE/STOP
        btnStopandSave.setDisable(false);
        btnPause.setDisable(false);
        btnUnpasue.setDisable(true);
        btnUnpasue.setStyle("-fx-background-color: red");
        //ustawienie kolorów tła pól tekstowych JavaFX
        txtCity.setStyle("-fx-background-color: white;");
        txtInterval.setStyle("-fx-background-color: white;");
        txtFileName.setStyle("-fx-background-color: white;");
        txtFileName.clear();
        //lista wyboru wypełnia się Stringami z listy obserwatorów:
        choiceOfCHart.setItems(choices);
        //czyszczenie wykresu:
        chart1.getData().removeAll(chart1.getData());
        //pobieram nawę miasta od użytkownika z pola tekstowego:
        String city = txtCity.getText();
        //wywołuje metodę sprawdzającą czy wpisane miasto jest na dostępnej liście
        checkCity(city,txtCity,txtCityException);
        //wartość "uśpienia programu jest w milisekundach dlatego pomnożone przez 1000:
        int interval = Integer.parseInt(txtInterval.getText()) * 1000;
        //sprawdzam poprawność kroku czasu wpisanego przez użytkownika
        timeCheck(interval, txtInterval,txtIntervalException);


        basic = new WeatherStation(city);//tworzę nowy obiekt klasy WeatherStation z polem miasto
        basic.setInterval(interval);//ustawiam krok "usypiania" programu
        basic.start();//uruchamiam program
        txtCity.setStyle("-fx-background-color: green;");
        txtInterval.setStyle("-fx-background-color: green;");
        data.clear();// data to pole klasy WeatherUpdate; czyszczę dane, które są na liście obserwatorów klasy WeatherBase
        WeatherUpdate weatherUpdate = new WeatherUpdate(data);
        basic.addObserver(weatherUpdate);//dodaje weatherUpdate jako obserwatora

        //ustawiam  dla komórek kolumn
        TableColumnLp.setCellValueFactory(new PropertyValueFactory<>("n"));
        TableColumnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumnTemp.setCellValueFactory(new PropertyValueFactory<>("temp"));
        TableColumnHum.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        TableColumnPress.setCellValueFactory(new PropertyValueFactory<>("pressure"));
        tabview.setItems(data);//tabela otrzymuje pole klasy WeatherUpdate

        //tworzę nowy wykres w zależności od kliknętego wyboru
        ChartDataDisplay chart = new ChartDataDisplay(chart1,yAxis,choiceOfCHart);
        //dodaje wykres do listy obserwatorów
        basic.addObserver(chart);
        //ustawiam skalowanie ticki tytuł dla wykresu:
        yAxis.setAutoRanging(true);
        yAxis.setTickUnit(1);
        xAxis.setAutoRanging(true);
        xAxis.setTickUnit(1);
        xAxis.setLabel("Measurement number");

        //dodaje obserwatora
        choiceOfCHart.getSelectionModel()
                .selectedItemProperty()//dla wybranej wartości z CHoicebox wywołuje metodę display
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> chart.display());

        //tworzę nowy obiekt, którego parametrami są pola tekstowe w JavaFX do aktualizowania
        TextFieldDisplay update = new TextFieldDisplay(txtNumber,  txtMaxTemp, txtMaxHum, txtMaxPress, txtSDtemp, txtSDhum, txtSDpress);
        basic.addObserver(update);//dla obiektu klasy WeatherStation update to tak na prawdke update weatherBase
    }

    /**
     * Methods start when button Pause is pressed. It pause downloading data from OpenWeather website.
     * @param event event
     */
    @FXML
    void btnPausePressed(ActionEvent event) {
        basic.stop();
        btnPause.setDisable(true);//PAUSE można wciśnąć tylko jeden raz
        btnPause.setStyle("-fx-background-color: red;");
        btnUnpasue.setDisable(false);
        btnUnpasue.setStyle("-fx-background-color: green;");
        btnStart.setStyle("-fx-background-color: green");
        btnStopandSave.setStyle("-fx-background-color: green");
    }

    /**
     * Method starts when button Stop and Save is pressed. It saves data as a file ".json"
     * @param event
     */
    @FXML
    void btnStopandSavePressed(ActionEvent event) {
        basic.interrupt();
        btnPause.setStyle("-fx-background-color: red;");
        btnUnpasue.setStyle("-fx-background-color: red;");
        btnStart.setStyle("-fx-background-color: red");
        btnStopandSave.setDisable(true);
        if (txtFileName.getText().equals("")) {
            txtFileName.setStyle("-fx-background-color: yellow;");
        } else {
            save(txtFileName.getText() + ".json");
        }
        txtCity.setEditable(true);
        txtInterval.setEditable(true);
        btnPause.setDisable(true);
        btnUnpasue.setDisable(true);
    }

    /**
     * Methods starts when button Unpause is pressed. It starts
     * @param event event
     */
    @FXML
    void btnUnpausePressed(ActionEvent event) {
        basic.start();
        btnStart.setDisable(true);
        btnStart.setStyle("-fx-background-color: red");
        btnUnpasue.setStyle("-fx-background-color: red;");
        btnPause.setStyle("-fx-background-color: green;");
        btnStopandSave.setStyle("-fx-background-color: green");
        btnUnpasue.setDisable(true);
        btnPause.setDisable(false);
    }


    @FXML
    void btnReadFilePressed(ActionEvent event) {

        chart1.getData().removeAll(chart1.getData());
        tabview.getItems().removeAll(tabview.getItems());

        ObservableList<WeatherBase> read = FXCollections.observableArrayList();
        try {
            WeatherBase[] baza = readFile(txtFileName1.getText()+".json");
            read.addAll(baza);

            XYChart.Series<Number, Number> tT = new XYChart.Series<>();
            XYChart.Series<Number, Number> tH = new XYChart.Series<>();
            XYChart.Series<Number, Number> tP = new XYChart.Series<>();

            for (int i = 0; i < read.size(); i++) {

                tT.getData().add(new XYChart.Data<>(read.get(i).getN(), read.get(i).getTemp()));
                tH.getData().add(new XYChart.Data<>(read.get(i).getN(), read.get(i).getHumidity()));
                tP.getData().add(new XYChart.Data<>(read.get(i).getN(), read.get(i).getPressure()));
            }

            TableColumnLp.setCellValueFactory(new PropertyValueFactory<>("n"));
            TableColumnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            TableColumnTemp.setCellValueFactory(new PropertyValueFactory<>("temp"));
            TableColumnHum.setCellValueFactory(new PropertyValueFactory<>("humidity"));
            TableColumnPress.setCellValueFactory(new PropertyValueFactory<>("pressure"));
            tabview.setItems(read);
            System.out.println(read.toString());

            if (choiceOfCHart.getValue().equals("Temperature")) {
                chart1.getData().addAll(tT);
                yAxis.setLabel("Temperature");
            } else if (choiceOfCHart.getValue().equals("Humidity")) {
               chart1.getData().addAll(tH);
               yAxis.setLabel("Humidity");
            } else {
                chart1.getData().addAll(tP);
               yAxis.setLabel("Pressure");
            }
            tT.setName("Temperature");
            tP.setName("Pressure");
            tH.setName("Humidity");
            tabview.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observableValue,s,t1) -> {
                       chart1.getData().removeAll(chart1.getData());
                        if (choiceOfCHart.getValue() == "Temperature") {
                            chart1.getData().addAll(tT);
                            yAxis.setLabel("Temperature");
                        } else if (choiceOfCHart.getValue() == "Humidity") {
                           yAxis.setLabel("Humidity");
                            chart1.getData().addAll(tH);
                        } else {
                            chart1.getData().addAll(tP);
                            yAxis.setLabel("Pressure");
                        }
                    });


            yAxis.setAutoRanging(true);
            yAxis.setTickUnit(1);
            yAxis.setAutoRanging(true);
            yAxis.setTickUnit(1);
            yAxis.setLabel("Measurement number");

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Method saves data to json file
     * @param fileName file name
     */
    public void save(String fileName) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(data, fileWriter);//zapisuje pole klasy WatherUpdate do pliku
            txtFileName.setStyle("-fx-background-color: pink;");
        } catch (IOException e) {
            System.out.println("Input/Output failure");
            txtFileName.setStyle("-fx-background-color: red;");
        }
    }


    /**
     * Method checks if time step is too small to update data.
     * @param interval
     * @param field
     * @param text
     */
    private void timeCheck(int interval, TextField field, Text text) {
        //jesli krok czasu za mały to wyświetla się błąd w postaci tekstu pod wpisanym czasem
        if (interval < 2000) {
            field.setStyle("-fx-background-color: red;");
            text.setText("Must be higher");
            throw new IllegalArgumentException("Interval too small");
        } else {
            field.setStyle("-fx-background-color: yellow;");
        }

    }


    private WeatherBase[] readFile(String name) {
        WeatherBase[] base = null;
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File file = new File(name);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            base = gson.fromJson(bufferedReader, WeatherBase[].class);
        } catch (IOException e) {
            txtFileName.setStyle("-fx-background-color: red;");
        }
       txtFileName.setStyle("-fx-background-color: white;");

        System.out.println(Arrays.toString(base));
        return base;
    }


    public void checkCity(String cityToCheck,TextField field,Text text) throws IllegalArgumentException {
        //metoda sprawdza czy miasto istnieje na liście dostępnych przez serwis OpenWeather
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        CheckCity[] check = null;
        boolean appears = false;// ta zmienna wskazuje czy miasto występuje na liście czy nie

        File file = new File("city.list.json");//plik z listą dostęonych miast w formacie json
        //wczytuje plik jsona z lista dostępnych miast
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //konwertuje z jsona na obiekty klasy CheckCity
            check = gson.fromJson(br, CheckCity[].class);
        } catch (FileNotFoundException e) {
            txtCity.setStyle(("-fx-background-color: red;"));
            e.printStackTrace();
        } catch (IOException e) {
            text.setStyle(("-fx-background-color: red;"));
            e.printStackTrace();
        }

        //pętla szuka po tabeli miasta
        for (int i = 0; i < check.length; i++) {
            if (check[i].getName().equals(cityToCheck)) {
                appears = true;
                break;
            }
        }

        //później zmienia kolor textFieldu
        if (appears) {
            text.setText("");
            field.setStyle(("-fx-background-color: yellow;"));
        } else {
            //jeśli miasta nie ma na liście to w tekście pod wpisanym miastem pojawia się message "Bad city name"
            text.setText("Bad city name");
            //pole tekstowe do wpisania miasta czerwieni się ze złości
            field.setStyle(("-fx-background-color: red;"));
            //w konsoli wyrzuca nam wyjątek:
            throw new IllegalArgumentException("There is no such city in a list");
        }
    }


}//koniec klasy Controller
