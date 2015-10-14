package weatherhandler.processor.query;

import java.io.PrintStream;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import weatherhandler.data.Measurement;
import weatherhandler.data.Stations;

import weatherhandler.processor.EachProcessor;
import weatherhandler.processor.query.group.Group;
import weatherhandler.processor.query.group.Average;
import weatherhandler.processor.query.group.Minimum;
import weatherhandler.processor.query.group.Maximum;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         Executes GROUP BY queries.
 *         Output is sent to an OutputStream in TSV format.
 *
 *         <T> is the type of the *group key*. This will probably usually be
 *         Integer, but it might be different for eg. grouping by the location
 *         of stations.
 *
 *         (It's basically MapReduce.)
 */
public class QueryExecuter extends EachProcessor implements AutoCloseable {
    /**
     * Predefined "groupers".
     */
    private static Map<String, Function<Measurement, Object>> groupers = new HashMap<>();
    /**
     * Predefined "mappers".
     */
    private static Map<String, ToDoubleFunction<Measurement>> mappers = new HashMap<>();
    /**
     * Predefined "reducers".
     */
    private static Map<String, Supplier<Group>> reducers = new HashMap<>();

    /**
     * Whether the predefined groupers/mappers/reducers have already been added.
     */
    private static boolean inited = false;
    /**
     * Predefines group/map/reduce functions if they haven't been initialised
     * yet.
     */
    private static void init() {
        if (inited) return;

        groupers.put("station", Measurement::getStation);
        groupers.put("country", m -> Stations.getStation(m.getStation()).getCountry());
        groupers.put("all", m -> 1);

        mappers.put("temperature", Measurement::getTemperature);
        mappers.put("dewpoint", Measurement::getDewPoint);
        mappers.put("stationpressure", Measurement::getStationPressure);
        mappers.put("sealevelpressure", Measurement::getSeaLevelPressure);
        mappers.put("visibility", Measurement::getVisibility);
        mappers.put("windspeed", Measurement::getWindSpeed);
        mappers.put("precipitation", Measurement::getPrecipitation);
        mappers.put("snowdepth", Measurement::getSnowDepth);
        mappers.put("cloudcover", Measurement::getCloudCover);
        mappers.put("winddirection", Measurement::getWindDirection);

        reducers.put("avg", Average::new);
        reducers.put("min", Minimum::new);
        reducers.put("max", Maximum::new);

        inited = true;
    }

    /**
     * Output stream to write the results to, in TSV format.
     */
    private PrintStream output = System.out;

    /**
     * Known groups.
     */
    private Map<Object, Group> groups = new HashMap<>();

    /**
     * Name of the predefined Grouper used for this query.
     */
    private String grouperName;
    /**
     * Name of the predefined Mapper used for this query.
     */
    private String mapperName;
    /**
     * Name of the predefined Reducer used for this query.
     */
    private String reducerName;

    /**
     * Function that returns a group key for a given measurement.
     */
    private Function<Measurement, Object> grouper;
    /**
     * Function that, given a measurement, returns the value to use for this
     * query's computation. This could simply be the temperature, for example,
     * but it can also be a more complex derived value, such as the ratio
     * between snow depth and temperature.
     */
    private ToDoubleFunction<Measurement> mapper;
    /**
     * Function that returns a new reducer for a group of mapped numbers.
     */
    private Supplier<Group> reducer;

    /**
     * Create a new Query, directing output to STDOUT.
     *
     * @param grouper
     *            Group key to use (country, station, etc).
     * @param mapper
     *            Measurement value to use (temperature, wind speed, etc).
     * @param reducer
     *            Computation to execute (avg, min, etc).
     */
    public QueryExecuter(String grouper, String mapper, String reducer) {
        init();

        this.grouperName = grouper;
        this.mapperName = mapper;
        this.reducerName = reducer;

        this.grouper = groupers.get(grouper);
        this.mapper = mappers.get(mapper);
        this.reducer = reducers.get(reducer);
    }

    /**
     * Process a new measurement, adding its mapped value to the relevant group.
     *
     * @param m
     *            Measurement.
     */
    public void processMeasurement(Measurement m) {
        this.groups.computeIfAbsent(this.grouper.apply(m), key -> this.reducer.get())
         .add(this.mapper.applyAsDouble(m));
    }

    /**
     * Print the result set to a file or stream in TSV format.
     */
    public void close() {
        // TSV header
        this.output.println(
            this.grouperName + "\t" +
            this.reducerName + "_" + this.mapperName
        );
        for (Object group : groups.keySet()) {
            this.output.println(String.join("\t", new String[] {
                "" + group,
                String.format("%.1f", groups.get(group).get())
            }));
        }
    }
}
