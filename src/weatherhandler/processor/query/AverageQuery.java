package weatherhandler.processor.query;

import java.io.OutputStream;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import weatherhandler.Logger;
import weatherhandler.data.Measurement;
import weatherhandler.data.Stations;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         Query class for computing averages by some grouping. Output is sent
 *         to an OutputStream in TSV format.
 * @param <T>
 *            Type
 */
public class AverageQuery<T> extends NumericGroupedQuery<T>implements AutoCloseable {
    /**
     * Used by NumericGroupedQuery for logging (potentially).
     */
    protected Logger logger = new Logger("AverageQuery");

    /**
     * Create a new AverageQuery, directing output to the given output stream.
     *
     * @param out
     *            Output stream.
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for the average
     *            computation.
     */
    public AverageQuery(OutputStream out, Function<Measurement, T> grouper, ToDoubleFunction<Measurement> mapper) {
        super(out, grouper, mapper);
    }

    /**
     * Create a new AverageQuery, directing output to standard output.
     *
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for the average
     *            computation.
     */
    public AverageQuery(Function<Measurement, T> grouper, ToDoubleFunction<Measurement> mapper) {
        super(grouper, mapper);
    }

    /**
     * @return Query that computes the average temperature for every station.
     */
    public static AverageQuery<Integer> temperatureByStation() {
        return new AverageQuery<>(m -> m.getStation(), Measurement::getTemperature);
    }

    /**
     * @return Query that computes the average temperature for every country.
     */
    public static AverageQuery<String> temperatureByCountry() {
        return new AverageQuery<>(m -> Stations.getStation(m.getStation()).getCountry(), Measurement::getTemperature);
    }

    /**
     * Create a new query group.
     *
     * @return Query group that computes the average of the given numbers.
     */
    protected Averager newGroup() {
        return new Averager();
    }

    /**
     * Print the result set to a file or stream in TSV format.
     */
    public void close() {
        this.output.println("group_key\taverage");
        for (T group : groups.keySet()) {
            this.output.println(String.join("\t", new String[] { "" + group, "" + groups.get(group).get() }));
        }
    }

    /**
     * Group class for computing averages.
     */
    private class Averager implements NumericGroupedQuery.Group {
        /**
         * Sum of the numbers seen so far.
         */
        private double sum = 0;

        /**
         * Amount of numbers seen so far.
         */
        private double count = 0;

        /**
         * Add a new number to the computation.
         *
         * @param n
         *            The new number.
         */
        public void add(double n) {
            sum += n;
            count++;
        }

        /**
         * @return The average of all added numbers.
         */
        public double get() {
            return sum / count;
        }
    }
}
