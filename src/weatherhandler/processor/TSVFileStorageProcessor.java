package weatherhandler.processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import weatherhandler.data.Measurement;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         {@link TSVFileStorageProcessor} will output {@link Measurement} to a
 *         TSV file
 */
public class TSVFileStorageProcessor implements Processor {
    private String fileName;

    /**
     * Constructor of {@link TSVFileStorageProcessor}
     * 
     * @param fileName
     *            the filename of the file that should be written to
     */
    public TSVFileStorageProcessor(String fileName) {
        this.fileName = fileName;
    }

    private String toTSV(Measurement m) {
        return String.join("\t", new String[] {
                "" + m.getStation(),
                "" + m.getDate(),
                "" + m.getTime(),
                "" + m.getTemperature(),
                "" + m.getDewPoint(),
                "" + m.getStationPressure(),
                "" + m.getSeaLevelPressure(),
                "" + m.getVisibility(),
                "" + m.getWindSpeed(),
                "" + m.getPrecipitation(),
                "" + m.getSnowDepth(),
                "" + m.getEvents(),
                "" + m.getCloudCover(),
                "" + m.getWindDirection()
        });
    }

    public synchronized void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.fileName, true)))) {
            for (Measurement m : measurements) {
                out.println(this.toTSV(m));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
