package weatherhandler;

import java.util.Map;

public class ServerStarter {
    public static void main(String[] args) {
        Map<String, String> options = new ArgParser(args).parse();

        new Thread(new WeatherServer(options)).start();
    }
}
