package weatherhandler.processor.query;

import java.io.OutputStream;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import weatherhandler.Logger;
import weatherhandler.data.Measurement;
import weatherhandler.processor.query.group.Minimum;

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
        super(out, grouper, mapper, Minimum::new);
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
        super(grouper, mapper, Minimum::new);
    }

    /**
     * @return Query that computes the lowest temperature ever measured.
     */
    public static MinQuery<Integer> temperature() {
        return new MinQuery<Integer>(m -> 1, Measurement::getTemperature);
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
}
