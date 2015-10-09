package weatherhandler.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marijn Pool
 * @author René Kooi
 * 
 *         MeasurementCache will create a LinkedList of Measurements for every
 *         station that exists
 */
public class MeasurementsCache {
    private static Map<Integer, List<Measurement>> cache = new HashMap<>();

    /**
     * Init the MeasurementCache
     */
    public static void init() {
        for (Station station : Stations.getStations()) {
            // TODO use a custom List class that also updates averages for every
            // data point whenever a new Measurement is added
            cache.put(station.getID(), new LinkedList<Measurement>());
        }
    }

    /**
     * @param station
     *            The station ID for the list that is returned
     * @return The List of Measurements that is returned based on the station ID
     */
    public static List<Measurement> getCache(Integer station) {
        return cache.get(station);
    }
}
