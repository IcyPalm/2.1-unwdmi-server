package weatherhandler.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import weatherhandler.Logger;

/**
 * @author Marijn Pool
 * @author René Kooi
 *
 *         The Database class is responsible for connecting to the PostgreSQL
 *         database
 *
 */
public class Database {
    // #Hardcoded
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weatherhandler";
    private static final String DB_USER = "weatherhandler";
    private static final String DB_PASS = "hanze";

    private static Connection connection = null;
    private static boolean createdTables = false;

    private static Logger logger = new Logger("Database");

    /**
     * Initialize a database connection.
     *
     * @return The database connection
     * @throws SQLException
     *             When something goes wrong with the connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL driver not found. Stack trace:");
            e.printStackTrace();
            System.exit(1);
        }
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return connection;
    }

    /**
     * Create tables when they do not exist
     *
     * @throws SQLException
     *             when something goes wrong
     */
    public static synchronized void createTables() throws SQLException {
        if (createdTables) {
            return;
        }
        Statement st = getConnection().createStatement();
        ResultSet res = st.executeQuery(
            "SELECT count(*) " +
            "FROM pg_tables " +
            "WHERE tablename = 'stations';"
        );
        boolean stationsExists = false;
        while (res.next()) {
            if (res.getInt(1) > 0) {
                stationsExists = true;
            }
        }
        res.close();

        if (!stationsExists) {
            st = getConnection().createStatement();
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS stations (\n" +
                "  id INT NOT NULL,\n" +
                "  name VARCHAR(64) NOT NULL,\n" +
                "  country VARCHAR(64) NOT NULL,\n" +
                "  latitude DOUBLE PRECISION NOT NULL,\n" +
                "  longitude DOUBLE PRECISION NOT NULL,\n" +
                "  elevation DOUBLE PRECISION NOT NULL,\n" +
                "  PRIMARY KEY(id)\n" +
                ");"
            );

            seedStations();
        }

        st = getConnection().createStatement();
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS weather_measurements (\n" +
            "  id SERIAL,\n" +
            "  station_id INT REFERENCES stations(id),\n" +
            "  time TIMESTAMP,\n" +
            "  temperature REAL,\n" +
            "  dew_point REAL,\n" +
            "  station_pressure REAL,\n" +
            "  sea_level_pressure REAL,\n" +
            "  visibility REAL,\n" +
            "  wind_speed REAL,\n" +
            "  precipitation REAL,\n" +
            "  snow_depth REAL,\n" +
            "  events VARCHAR(12),\n" + // this VARCHAR(12) is TEMPORARILY
                                        // PLEASE
            "  cloud_cover REAL,\n" +
            "  wind_direction INT,\n" +
            "  PRIMARY KEY (id)\n" +
            ");"
        );
        createdTables = true;
    }

    private static void seedStations() throws SQLException {
        PreparedStatement st = getConnection().prepareStatement(
            "INSERT INTO stations (id, name, country, latitude, longitude, elevation) " +
            "VALUES (?, ?, ?, ?, ?, ?)"
        );
        // #Hardcoded :(
        try {
            BufferedReader lines = new BufferedReader(new FileReader("./stations.tsv"));
            String line;
            while ((line = lines.readLine()) != null) {
                String[] data = line.split("\\t");
                st.setInt(1, Integer.parseInt(data[0], 10));
                st.setString(2, data[1]);
                st.setString(3, data[2]);
                st.setDouble(4, Double.parseDouble(data[3]));
                st.setDouble(5, Double.parseDouble(data[4]));
                st.setDouble(6, Double.parseDouble(data[5]));
                st.addBatch();
            }
            st.executeBatch();
            lines.close();
        } catch (IOException e) {
            logger.error("Could not seed stations table.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
