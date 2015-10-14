package weatherhandler.processor.query;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import weatherhandler.Logger;
import weatherhandler.data.Measurement;
import weatherhandler.processor.EachProcessor;
import weatherhandler.processor.query.group.Group;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         Abstract class for building grouped queries, similar to SQL GROUP BY.
 *         Output is sent to an OutputStream in TSV format. Implementors need to
 *         implement the grouping logic and the mapping logic (i.e. deriving a
 *         useful value from measurements).
 *
 *         <T> is the type of the *group key*. This will probably usually be
 *         Integer, but it might be different for eg. grouping by the location
 *         of stations.
 *
 *         (It's basically MapReduce.)
 * @param <T>
 *            Type
 */
public class NumericGroupedQuery<T> extends EachProcessor {
    /**
     * Known groups.
     */
    protected Map<T, Group> groups = new HashMap<>();
    /**
     * Currently unused...
     */
    protected Logger logger = new Logger("NumericGroupedQuery");
    /**
     * Output stream. The TSV data will be printed to here.
     */
    protected PrintStream output;

    /**
     * Function that returns a group key for a given measurement.
     */
    protected Function<Measurement, T> grouper;
    /**
     * Function that, given a measurement, returns the value to use for this
     * query's computation. This could simply be the temperature, for example,
     * but it can also be a more complex derived value, such as the ratio
     * between snow depth and temperature.
     */
    protected ToDoubleFunction<Measurement> mapper;
    /**
     * Function that returns a new reducer for a group of mapped numbers.
     */
    protected Supplier<Group> reducer;

    /**
     * Create a new Query, directing output to the given output stream.
     *
     * @param out
     *            Output stream.
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for this query's
     *            computation.
     * @param reducer
     *            Function that creates a new Group class.
     */
    public NumericGroupedQuery(
        OutputStream out,
        Function<Measurement, T> grouper,
        ToDoubleFunction<Measurement> mapper,
        Supplier<Group> reducer
    ) {
        this.output = new PrintStream(out);
        this.grouper = grouper;
        this.mapper = mapper;
        this.reducer = reducer;
    }

    /**
     * Create a new AverageQuery, directing output to standard output.
     *
     * @param grouper
     *            Function that returns a group key for a given measurement.
     * @param mapper
     *            Function that returns the number to use for the average
     *            computation.
     * @param reducer
     *            Function that creates a new Group class.
     */
    public NumericGroupedQuery(
        Function<Measurement, T> grouper,
        ToDoubleFunction<Measurement> mapper,
        Supplier<Group> reducer
    ) {
        this.output = System.out;
        this.grouper = grouper;
        this.mapper = mapper;
        this.reducer = reducer;
    }

    /**
     * Process a new measurement, adding its mapped value to the relevant group.
     *
     * @param m
     *            Measurement.
     */
    public void processMeasurement(Measurement m) {
        groups.computeIfAbsent(this.grouper.apply(m), key -> this.reducer.get())
         .add(this.mapper.applyAsDouble(m));
    }
}
