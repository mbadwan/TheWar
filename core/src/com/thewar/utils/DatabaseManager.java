package com.thewar.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manages database connections and operations for the game.
 * This class handles connecting to the database, creating necessary tables, and closing the database connection.
 */
public class DatabaseManager {
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);


    /**
     * Initializes the database connection and creates necessary tables.
     */    static {
        connect();
        createResourcesTable();
    }
    /**
     * Establishes a connection to the database using configuration from a properties file.
     */
    public static void connect() {
        try {
            // Load the properties file
            InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(input);

            // Get the database connection details
            String url = prop.getProperty("database.url");

            // Connect to the database
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to database.");

            // Create the 'users' table if it does not exist
            createTable();
        } catch (Exception e) {
            logger.error("An error occurred while adding the user", e);

        }
    }
    /**
     * Creates the 'user_resources' table if it does not exist.
     */
    public static void createResourcesTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS user_resources (" +
                    "user_id INT PRIMARY KEY," +
                    "silver_amount INT NOT NULL," +
                    "iron_amount INT NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))";
            stmt.executeUpdate(sql);
            System.out.println("User resources table created (if not exists).");
        } catch (SQLException e) {
            logger.error("An error occurred while creating the user_resources table", e);
        }
    }

    /**
     * Creates the 'users' table if it does not exist.
     */
    public static void createTable() {
        try {
            Statement statement = connection.createStatement();
            // Create user table
            String users = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL" +
                    ")";
            statement.executeUpdate(users);
            System.out.println("User table created (if not exists).");
        } catch (SQLException e) {
            logger.error("An error occurred while adding the user", e);

        }
    }
    /**
     * Returns the current database connection.
     *
     * @return The current database connection.
     */
    public static Connection getConnection() {
        return connection;
    }
    /**
     * Closes the database connection.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("An error occurred while adding the user", e);

        }
    }
}
