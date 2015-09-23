package weatherhandler.processor;

import weatherhandler.database.Database;
import weatherhandler.data.Measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public DBStorageProcessor(String tableName) {
        this.tableName = tableName;

        try {
            this.connection = Database.getConnection();
            this.dropTableWhileDebugging();
            this.createTableIfNecessary();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropTableWhileDebugging() throws SQLException {
        Statement st = this.connection.createStatement();
        st.executeUpdate("DROP TABLE IF EXISTS " + this.tableName + ";");
    }

    private void createTableIfNecessary() throws SQLException {
        Statement st = this.connection.createStatement();
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS " + this.tableName + " (\n" +
            "  id SERIAL,\n" +
            "  station_id INT,\n" +
            "  time TIMESTAMP,\n" +
            // All these are VARCHAR(12) TEMPORARILY PLEASE
            // At least, I hope so! It'd be pretty terrible if they weren't!
            "  temperature VARCHAR(12),\n" +
            "  dew_point VARCHAR(12),\n" +
            "  station_pressure VARCHAR(12),\n" +
            "  sea_level_pressure VARCHAR(12),\n" +
            "  visibility VARCHAR(12),\n" +
            "  wind_speed VARCHAR(12),\n" +
            "  precipitation VARCHAR(12),\n" +
            "  snow_depth VARCHAR(12),\n" +
            "  events VARCHAR(12),\n" +
            "  cloud_cover VARCHAR(12),\n" +
            "  wind_direction VARCHAR(12),\n" +
            "  PRIMARY KEY (id)\n" +
            ");"
        );
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
                st.setInt(STATION_ID, Integer.parseInt(m.getStation(), 10));
                Date time;
                try {
                    time = formatter.parse(m.getDate() + " " + m.getTime());
                } catch (ParseException e) {
                    // default to RIGHT NOW. :eyes:
                    time = new Date();
                }
                st.setTimestamp(TIME, new Timestamp(time.getTime()));
                st.setString(TEMPERATURE, m.getTemperature());
                st.setString(DEW_POINT, m.getDewPoint());
                st.setString(STATION_PRESSURE, m.getStationPressure());
                st.setString(SEA_LEVEL_PRESSURE, m.getSeaLevelPressure());
                st.setString(VISIBILITY, m.getVisibility());
                st.setString(WIND_SPEED, m.getWindSpeed());
                st.setString(PRECIPITATION, m.getPrecipitation());
                st.setString(SNOW_DEPTH, m.getSnowDepth());
                st.setString(EVENTS, m.getEvents());
                st.setString(CLOUD_COVER, m.getCloudCover());
                st.setString(WIND_DIRECTION, m.getWindDirection());

                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new ProcessorException(e);
        }
    }
}
