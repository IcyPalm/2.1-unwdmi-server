package weatherhandler.data;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import weatherhandler.database.Database;

public class MeasurementsCache {
    private static Map<Integer, List<Measurement>> cache = new HashMap<>();

    public static void init() throws SQLException {
        Statement st = Database.getConnection().createStatement();
        ResultSet rs = st.executeQuery("SELECT id FROM stations");
        while (rs.next()) {
            int station = rs.getInt(1);
            cache.put(station, new LinkedList<Measurement>());
        }
    }

    public static List<Measurement> getCache(Integer station) {
        return cache.get(station);
    }
}
