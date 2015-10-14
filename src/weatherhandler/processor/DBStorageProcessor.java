package weatherhandler.processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import weatherhandler.data.Measurement;
import weatherhandler.database.Database;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         This class will receive processed measurements and insert them into
 *         the database
 */
public class DBStorageProcessor implements Processor {
    final private static int STATION_ID = 1;
    final private static int TIME = 2;
    final private static int TEMPERATURE = 3;
    final private static int DEW_POINT = 4;
    final private static int STATION_PRESSURE = 5;
    final private static int SEA_LEVEL_PRESSURE = 6;
    final private static int VISIBILITY = 7;
    final private static int WIND_SPEED = 8;
    final private static int PRECIPITATION = 9;
    final private static int SNOW_DEPTH = 10;
    final private static int EVENTS = 11;
    final private static int CLOUD_COVER = 12;
    final private static int WIND_DIRECTION = 13;

    private Connection connection;
    private String tableName;

    /**
     * Create a new {@link DBStorageProcessor} that will enter measurements in
     * the table
     *
     * @param tableName
     *            The Table name that is used to store the measurements
     */
    public DBStorageProcessor(String tableName) {
        this.tableName = tableName;

        try {
            this.connection = Database.getConnection();
            Database.createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO use Measurements wrapped in WeatherData batch class
    public void processMeasurements(List<Measurement> measurements) throws ProcessorException {
        try {
            PreparedStatement st = this.connection.prepareStatement(
                "INSERT INTO " + this.tableName + " (" +
                    "station_id, time, temperature, dew_point, station_pressure, sea_level_pressure, " +
                    "visibility, wind_speed, precipitation, snow_depth, events, cloud_cover, wind_direction" +
                ") VALUES (" +
                    "?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?" +
                ")"
            );

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Measurement m : measurements) {
                st.setInt(STATION_ID, m.getStation());
                Date time;
                try {
                    time = formatter.parse(m.getDate() + " " + m.getTime());
                } catch (ParseException e) {
                    // default to RIGHT NOW. :eyes:
                    time = new Date();
                }
                st.setTimestamp(TIME, new Timestamp(time.getTime()));
                st.setFloat(TEMPERATURE, m.getTemperature());
                st.setFloat(DEW_POINT, m.getDewPoint());
                st.setFloat(STATION_PRESSURE, m.getStationPressure());
                st.setFloat(SEA_LEVEL_PRESSURE, m.getSeaLevelPressure());
                st.setFloat(VISIBILITY, m.getVisibility());
                st.setFloat(WIND_SPEED, m.getWindSpeed());
                st.setFloat(PRECIPITATION, m.getPrecipitation());
                st.setFloat(SNOW_DEPTH, m.getSnowDepth());
                st.setString(EVENTS, m.getEvents());
                st.setFloat(CLOUD_COVER, m.getCloudCover());
                st.setInt(WIND_DIRECTION, m.getWindDirection());

                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new ProcessorException(e);
        }
    }
}
