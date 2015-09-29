package weatherhandler;

public class ServerStarter {
    public static void main(String[] args) {
        new Thread(new WeatherServer()).start();
    }
}
