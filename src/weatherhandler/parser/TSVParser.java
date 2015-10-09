package weatherhandler.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.AutoCloseable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;
import weatherhandler.processor.Processor;
import weatherhandler.processor.ProcessorException;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 * TSVParser is a class that processes Tab Separated Value files containing
 * measurement data.
 * 
 */
public class TSVParser implements AutoCloseable {
    private Logger logger = new Logger("TSVParser");
    private BufferedReader reader;
    private Stream<String> stream;
    private Processor output;

    /**
     * Create a new TSVParser with a filename
     * @param fileName The File you want to read from
     * @param out The processor that will receive the data
     * @throws FileNotFoundException if the file is not found
     */
    public TSVParser(String fileName, Processor out) throws FileNotFoundException {
        this(new FileReader(fileName), out);
    }
    /**
     * Create a new TSVParser with a buffered reader instead of a file
     * @param reader the input reader
     * @param out The processor that will receive the data
     */
    public TSVParser(Reader reader, Processor out) {
        this.reader = new BufferedReader(reader);
        this.stream = this.reader.lines();
        this.output = out;
    }

    /**
     * Process the given TSV data
     */
    public void process() {
        this.stream
            .map(this::parse)
            .map(this::toList)
            .forEach(this::processMeasurements);
        this.done();
    }

    public void close() throws IOException {
        this.reader.close();
    }

    private List<Measurement> toList(Measurement m) {
        List<Measurement> list = new ArrayList<>(1);
        list.add(m);
        return list;
    }

    private void processMeasurements(List<Measurement> measurements) {
        try {
            this.output.processMeasurements(measurements);
        } catch (ProcessorException e) {
            this.logger.error("Processor error:");
            e.printStackTrace();
        }
    }

    private void done() {
        if (this.output instanceof AutoCloseable) {
            try {
                ((AutoCloseable) this.output).close();
            } catch (Exception e) {
                this.logger.error("Close error:");
                e.printStackTrace();
            }
        }
    }

    private Measurement parse(String line) {
        Measurement m = new Measurement();

        String[] data = line.split("\t");
        m.setStation(Integer.parseInt(data[0], 10));
        m.setDate(data[1]);
        m.setTime(data[2]);
        m.setTemperature(Float.parseFloat(data[3]));
        m.setDewPoint(Float.parseFloat(data[4]));
        m.setStationPressure(Float.parseFloat(data[5]));
        m.setSeaLevelPressure(Float.parseFloat(data[6]));
        m.setVisibility(Float.parseFloat(data[7]));
        m.setWindSpeed(Float.parseFloat(data[8]));
        m.setPrecipitation(Float.parseFloat(data[9]));
        m.setSnowDepth(Float.parseFloat(data[10]));
        m.setEvents(data[11]);
        m.setCloudCover(Float.parseFloat(data[12]));
        m.setWindDirection(Integer.parseInt(data[13], 10));

        return m;
    }
}
