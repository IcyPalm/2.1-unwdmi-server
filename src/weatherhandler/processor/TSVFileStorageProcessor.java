package weatherhandler.processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;

public class TSVFileStorageProcessor implements Processor {
    private String fileName;
    private Logger logger = new Logger("TSVFileStorage");

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

    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        try (PrintWriter out = new PrintWriter(
            new BufferedWriter(
                new FileWriter(this.fileName, true)))) {
            for (Measurement m : measurements) {
                out.println(this.toTSV(m));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
