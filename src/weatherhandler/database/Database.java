package weatherhandler.database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Database {
    // #Hardcoded
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weatherhandler";
    private static final String DB_USER = "weatherhandler";
    private static final String DB_PASS = "hanze";

    private static Connection connection = null;
    private static boolean createdTables = false;

    /**
     * Initialize a database connection.
     */
    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL driver not found. Stack trace:");
            e.printStackTrace();
            System.exit(1);
        }
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return connection;
    }

    public static synchronized void createTables() throws SQLException {
        if (createdTables) {
            return;
        }
        Statement st = getConnection().createStatement();
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS weather_measurements (\n" +
            "  id SERIAL,\n" +
            "  station_id INT,\n" +
            "  time TIMESTAMP,\n" +
            "  temperature REAL,\n" +
            "  dew_point REAL,\n" +
            "  station_pressure REAL,\n" +
            "  sea_level_pressure REAL,\n" +
            "  visibility REAL,\n" +
            "  wind_speed REAL,\n" +
            "  precipitation REAL,\n" +
            "  snow_depth REAL,\n" +
            "  events VARCHAR(12),\n" + // this VARCHAR(12) is TEMPORARILY PLEASE
            "  cloud_cover REAL,\n" +
            "  wind_direction INT,\n" +
            "  PRIMARY KEY (id)\n" +
            ");"
        );
        createdTables = true;
    }
}
