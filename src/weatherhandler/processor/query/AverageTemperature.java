package weatherhandler.processor.query;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;
import java.lang.AutoCloseable;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;
import weatherhandler.processor.EachProcessor;

public class AverageTemperature extends EachProcessor implements AutoCloseable {
    private Map<Integer, Averager> averages = new HashMap<>();
    private Logger logger = new Logger("AverageTemperature");
    private PrintStream output;

    public AverageTemperature(OutputStream out) {
        this.output = new PrintStream(out);
    }
    public AverageTemperature() {
        this.output = System.out;
    }

    public void processMeasurement(Measurement m) {
        averages.computeIfAbsent(m.getStation(), key -> new Averager())
            .add(m.getTemperature());
    }

    public void close() {
        this.output.println("station\taverage_temperature");
        for (int station : averages.keySet()) {
            this.output.println(
                String.join("\t", new String[] {
                    "" + station,
                    "" + averages.get(station).get()
                })
            );
        }
    }

    private class Averager {
        private float sum = 0;
        private float count = 0;
        public void add(float n) {
            sum += n;
            count++;
        }
        public float get() {
            return sum / count;
        }
    }
}
