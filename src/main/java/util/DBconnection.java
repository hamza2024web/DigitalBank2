package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private static DBconnection instance;
    private static Connection connection;

    private final String username = "admin";
    private final String password = "admin@admin.com";
    private final String url = "jdbc:postgresql://localhost:5433/digitalBank";

    private DBconnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    public static DBconnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBconnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
