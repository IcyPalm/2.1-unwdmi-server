package weatherhandler.processor.query;

import java.io.OutputStream;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import weatherhandler.Logger;
import weatherhandler.data.Measurement;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         Query class for computing the lowest value by some grouping. Output
 *         is sent to an OutputStream in TSV format.
 * @param <T>
 *            Type
 */
public class MinQuery<T> extends NumericGroupedQuery<T>implements AutoCloseable {
    /**
     * Used by NumericGroupedQuery for logging (potentially).
     */
    protected Logger logger = new Logger("MinQuery");

    /**
     * Create a new MinQuery, directing output to the given output stream.
     *
     * @param out
     *            Output stream.
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for the min()
     *            computation.
     */
    public MinQuery(OutputStream out, Function<Measurement, T> grouper, ToDoubleFunction<Measurement> mapper) {
        super(out, grouper, mapper);
    }

    /**
     * Create a new MinQuery, directing output to standard output.
     *
     * @param out
     *            Output stream.
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for the min()
     *            computation.
     */
    public MinQuery(Function<Measurement, T> grouper, ToDoubleFunction<Measurement> mapper) {
        super(grouper, mapper);
    }

    /**
     * @return Query that computes the lowest temperature ever measured.
     */
    public static MinQuery<Integer> temperature() {
        return new MinQuery<Integer>(m -> 1, Measurement::getTemperature);
    }

    /**
     * Create a new query group.
     *
     * @return Query group that returns the lowest value in the given numbers.
     */
    protected Minimum newGroup() {
        return new Minimum();
    }

    /**
     * Print the result set to a file or stream in TSV format.
     */
    public void close() {
        this.output.println("group_key\tminimum");
        for (T group : groups.keySet()) {
            this.output.println(
                    String.join("\t", new String[] { "" + group, String.format("%.1f", groups.get(group).get()) }));
        }
    }

    /**
     * Group class for computing minima.
     */
    protected class Minimum implements NumericGroupedQuery.Group {
        /**
         * Lowest number seen so far.
         */
        private double min = Double.MAX_VALUE;

        /**
         * Check a new contestant for the title of "Lowest Number Known To This
         * Group".
         *
         * @param n
         *            The daring new contestant.
         */
        public void add(double n) {
            if (n < min) {
                min = n;
            }
        }

        /**
         * @return The lowest value in this group.
         */
        public double get() {
            return min;
        }
    }
}
