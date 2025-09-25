package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static JDBCUtil instance;
    private Connection connection;

    private final String username = "admin";
    private final String password = "admin@admin.com";
    private final String url = "jdbc:postgresql://localhost:5433/digitalBank";

    private JDBCUtil() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Postgres");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    public static synchronized JDBCUtil getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new JDBCUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

