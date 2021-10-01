
package c195.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class makes the database connection
 * to the DB_URL with the corresponding configuration
 */
public class ConnectDB {
    private static final String databaseName = "agbi";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + databaseName;
    private static final String username = "agbi";
    private static final String password = "agbi";
    private static final String driver = "com.mysql.jdbc.Driver";
    public static Connection conn;

    public static Connection makeConnection() throws ClassNotFoundException, SQLException, Exception {
        Class.forName(driver);
        conn = DriverManager.getConnection(DB_URL, username, password);
        return conn;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }
}