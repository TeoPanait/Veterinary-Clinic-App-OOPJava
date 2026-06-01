package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// utility class for database connectivity (not singleton itself, but centralizes connection parameters)
// provides single source of truth for database credentials and connection string
public class DbConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/vet_clinic";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    // private constructor: prevents instantiation (utility class)
    private DbConnection() {}

    // create and return a new database connection
    // throws SQLException if connection fails
    // each call creates a new connection (not pooled in this implementation)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
