package weatherhandler;

import weatherhandler.data.MeasurementsCache;
import weatherhandler.data.Stations;
import weatherhandler.Logger;
import weatherhandler.parser.TSVParser;
import weatherhandler.parser.WeatherParser;
import weatherhandler.processor.Processor;
import weatherhandler.processor.BatchUpdatesProcessor;
import weatherhandler.processor.CompleteMissingProcessor;
import weatherhandler.processor.DBStorageProcessor;
import weatherhandler.processor.NullProcessor;
import weatherhandler.processor.TSVFileStorageProcessor;
import weatherhandler.processor.UpdatesMonitor;

import weatherhandler.processor.query.AverageQuery;
import weatherhandler.processor.query.MinQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Map;


/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 * WeatherServer will open a ServerSocket on port 7789 
 * and wait for any incoming weatherstation connections
 */
public class WeatherServer implements Runnable {
    final public static int SERVER_PORT = 7789;

    private Map<String, String> options;
    private Socket socket;
    private ServerSocket TCPsocket;
    private Logger logger = new Logger("Server");

    public WeatherServer(Map<String, String> options) {
        this.options = options;
    }

    @Override
    public void run() {
        //Load the StationList from File
        Stations.loadFromTSV("./stations.tsv");
        MeasurementsCache.init();

        this.logger.info("Stations loaded");
        this.logger.info(options.toString());

        // Set up output processing chain
        Processor processor = new NullProcessor();
        if (options.containsKey("save")) {
            processor = this.options.get("save").equalsIgnoreCase("postgres")
                      ? new DBStorageProcessor(options.getOrDefault("table", "weather_measurements"))
                      : new TSVFileStorageProcessor(options.getOrDefault("file", "measurements.tsv"));

            int batchSize = Integer.parseInt(options.getOrDefault("batch", "2000"), 10);
            if (batchSize > 0) {
                processor = new BatchUpdatesProcessor(batchSize, processor);
            }

            int monitorInterval = Integer.parseInt(options.getOrDefault("monitor", "5"), 10);
            if (monitorInterval > 0) {
                processor = new UpdatesMonitor(monitorInterval * 1000, processor);
            }
        } else if (options.containsKey("query")) {
            switch (options.get("query")) {
            case "avg:temperatureByStation":
                processor = AverageQuery.temperatureByStation();
                break;
            case "avg:temperatureByCountry":
                processor = AverageQuery.temperatureByCountry();
                break;
            case "min:temperature":
                processor = MinQuery.temperature();
                break;
            default:
                System.err.println("Unknown query \"" + options.get("query") + "\"");
                System.exit(1);
            }
        } else {
            this.logger.warn("No final processor set: Using NullProcessor");
        }

        // Set up input data
        if (options.containsKey("load")) {
            try (TSVParser parser = new TSVParser(options.get("load"), processor)) {
                parser.process();
            } catch (IOException e) {
                this.logger.error("TSV Parser error:");
                e.printStackTrace();
                System.exit(1);
            }
        } else {

            try {
                TCPsocket = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.logger.info("Listening on " + SERVER_PORT);

            int clients = 0;
            while (true) {
                try {
                    socket = TCPsocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Runnable thread = new ServerHandler(socket,
                    new CompleteMissingProcessor(30, processor));
                new Thread(thread).start();
                clients++;
                if (clients % 50 == 0) {
                    this.logger.debug("New Client (" + clients + " connected)");
                }
            }
        }
    }

    /**
     * Close ServerSocket
     */
    public void interrupt() {
        try {
            TCPsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerHandler implements Runnable {

    private Socket socket;
    private Processor processor;

    public ServerHandler(Socket socket, Processor processor) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        String line;
        StringBuilder lines = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Read data client sends to server
            while ((line = in.readLine()) != null) {
                lines.append(line);
                // if </WEATHERDATA> is encountered close document and create new Parser.
                if (line.contains("</WEATHERDATA>")) {
                    new WeatherParser(lines.toString(), this.processor);
                    lines.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            // Close connection
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
