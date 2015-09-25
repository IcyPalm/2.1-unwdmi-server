package weatherhandler;

public class Serverstarter {
    public static void main(String[] args) {
        new Thread(new WeatherServer()).start();
    }
}
