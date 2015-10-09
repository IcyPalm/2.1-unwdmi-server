package weatherhandler.data;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import weatherhandler.data.Station;
import weatherhandler.database.Database;
import weatherhandler.Logger;

public class Stations {
    private static Map<Integer, Station> stations = new HashMap<>();
    private static Logger logger = new Logger("Stations");

    public static Station getStation(int id) {
        return stations.get(id);
    }

    public static Collection<Station> getStations() {
        return stations.values();
    }

    public static void loadFromTSV(String fileName) {
        try (BufferedReader lines = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = lines.readLine()) != null) {
                Station station = new Station();
                String[] data = line.split("\\t");
                station.setID(Integer.parseInt(data[0], 10));
                station.setName(data[1]);
                station.setCountry(data[2]);
                station.setLatitude(Float.parseFloat(data[3]));
                station.setLongitude(Float.parseFloat(data[4]));
                station.setElevation(Float.parseFloat(data[5]));
                stations.put(station.getID(), station);
            }
        } catch (IOException e) {
            logger.error("Could not load stations from TSV file:");
            e.printStackTrace();
        }
    }

    public static void loadFromDatabase() {
        try (Statement st = Database.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM stations");
            while (rs.next()) {
                Station station = new Station();
                station.setID(rs.getInt(1));
                station.setName(rs.getString(2));
                station.setCountry(rs.getString(3));
                station.setLatitude((float) rs.getDouble(4));
                station.setLongitude((float) rs.getDouble(5));
                station.setElevation((float) rs.getDouble(6));
                stations.put(station.getID(), station);
            }
        } catch (SQLException e) {
            logger.error("Could not load stations from database:");
            e.printStackTrace();
        }
    }
}
