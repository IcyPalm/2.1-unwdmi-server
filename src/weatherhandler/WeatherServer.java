package weatherhandler;

import java.io.IOException;
import java.util.Map;

import weatherhandler.data.MeasurementsCache;
import weatherhandler.data.Stations;
import weatherhandler.parser.Parser;
import weatherhandler.parser.TSVParser;
import weatherhandler.parser.ServerParser;
import weatherhandler.processor.BatchUpdatesProcessor;
import weatherhandler.processor.CompleteMissingProcessor;
import weatherhandler.processor.DBStorageProcessor;
import weatherhandler.processor.NullProcessor;
import weatherhandler.processor.Processor;
import weatherhandler.processor.TSVFileStorageProcessor;
import weatherhandler.processor.UpdatesMonitor;
import weatherhandler.processor.query.group.Average;
import weatherhandler.processor.query.group.Minimum;
import weatherhandler.processor.query.group.Maximum;
import weatherhandler.processor.query.QueryExecuter;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         WeatherServer will open a ServerSocket on port 7789 and wait for any
 *         incoming weatherstation connections
 */
public class WeatherServer implements Runnable {
    /**
     * The Server port
     */
    final public static int SERVER_PORT = 7789;

    private Map<String, String> options;
    private Parser parser;
    private Logger logger = new Logger("Server");

    /**
     * Constructor for the {@link WeatherServer} with the option to give params
     *
     * @param options
     *            The params that are passed from {@link ServerStarter}
     */
    public WeatherServer(Map<String, String> options) {
        this.options = options;
    }

    @SuppressWarnings("resource")
    @Override
    public void run() {
        // Load the StationList from File
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
                processor = new QueryExecuter("station", "temperature", "avg");
                break;
            case "avg:temperatureByCountry":
                processor = new QueryExecuter("country", "temperature", "avg");
                break;
            case "min:temperature":
                processor = new QueryExecuter("all", "temperature", "min");
                break;
            case "max:temperature":
                processor = new QueryExecuter("all", "temperature", "max");
                break;
            default:
                String grouper = options.getOrDefault("group-by", "all");
                String reducer = options.get("query");
                String mapper = options.getOrDefault("value", "temperature");
                processor = new QueryExecuter(grouper, mapper, reducer);
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
            // "processor" does not change here anymore,
            // this is to tell Java that it _really_ won't
            Processor proc = processor;
            parser = new ServerParser(SERVER_PORT,
                                      () -> new CompleteMissingProcessor(30, proc));
            parser.process();
        }
    }

    /**
     * Close Parser.
     */
    public void interrupt() {
        if (this.parser != null && this.parser instanceof AutoCloseable) {
            try {
                this.parser.close();
            } catch (Exception e) {
                this.logger.error("Interrupt error:");
                e.printStackTrace();
            }
        }
    }
}


