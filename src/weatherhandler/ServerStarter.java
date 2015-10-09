package weatherhandler;

import java.util.Map;


/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 * ServerStarter will launch a WeatherServer
 */
public class ServerStarter {
    /**
     * Main method, will launch {@link @WeatherServer}
     * @param args NOT IMPLEMENTED
     */
    public static void main(String[] args) {
        Map<String, String> options = new ArgParser(args).parse();

        new Thread(new WeatherServer(options)).start();
    }
}
