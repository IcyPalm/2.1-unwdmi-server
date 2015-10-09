package weatherhandler.processor.query;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.lang.AutoCloseable;

import weatherhandler.data.Measurement;
import weatherhandler.Logger;
import weatherhandler.processor.EachProcessor;

public class AverageQuery<T> extends EachProcessor implements AutoCloseable {
    private Map<T, Averager> averages = new HashMap<>();
    private Logger logger = new Logger("AverageQuery");
    private PrintStream output;

    private Function<Measurement, T> grouper;
    private ToDoubleFunction<Measurement> mapper;

    public AverageQuery(
        OutputStream out,
        Function<Measurement, T> grouper,
        ToDoubleFunction<Measurement> mapper
    ) {
        this.output = new PrintStream(out);
        this.grouper = grouper;
        this.mapper = mapper;
    }
    public AverageQuery(
        Function<Measurement, T> grouper,
        ToDoubleFunction<Measurement> mapper
    ) {
        this.output = System.out;
        this.grouper = grouper;
        this.mapper = mapper;
    }

    public static AverageQuery<Integer> temperatureByStation() {
        return new AverageQuery<>(
            m -> m.getStation(),
            Measurement::getTemperature
        );
    }

    public void processMeasurement(Measurement m) {
        averages.computeIfAbsent(this.grouper.apply(m), key -> new Averager())
            .add(this.mapper.applyAsDouble(m));
    }

    public void close() {
        this.output.println("group_key\taverage");
        for (T group : averages.keySet()) {
            this.output.println(
                String.join("\t", new String[] {
                    "" + group,
                    "" + averages.get(group).get()
                })
            );
        }
    }

    private class Averager {
        private double sum = 0;
        private double count = 0;
        public void add(double n) {
            sum += n;
            count++;
        }
        public double get() {
            return sum / count;
        }
    }
}
