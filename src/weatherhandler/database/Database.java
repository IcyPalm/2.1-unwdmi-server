package weatherhandler.database;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // #Hardcoded
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weatherhandler";
    private static final String DB_USER = "weatherhandler";
    private static final String DB_PASS = "hanze";

    private static Connection connection = null;

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
}
