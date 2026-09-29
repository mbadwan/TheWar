package com.thewar.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Provides methods for manipulating data in the database.
 * This includes adding users, checking if a user exists, retrieving user IDs, and updating resources.
 */
public class DataManipulator {
    private static final Logger logger = LogManager.getLogger(DataManipulator.class);
    /**
     * Adds a new user to the database.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return true if the user was added successfully, false otherwise.
     */
    public static boolean addUser(String username, String password) {
        // Initialize the DatabaseManager
        DatabaseManager.connect();

        Connection connection = DatabaseManager.getConnection();
        if (connection != null) {
            try {
                /**
                 * here we are checking first if the username already in database
                 */
                String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();

                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    logger.info("Username already exists.");
                } else {
                    // Prepare the SQL statement
                    String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    logger.info("Prepared statement ready.");
                    preparedStatement.executeUpdate();
                    logger.info("User added successfully.");
                }
            } catch (SQLException e) {
                logger.error("An error occurred while adding the user", e);
            }
        } else {
            logger.error("Failed to add user. No database connection.");
        }
        return false;
    }  /**
     * Checks if a user exists in the database.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public static boolean userExists(String username) {
        Connection connection = DatabaseManager.getConnection();
        if (connection != null) {
            try {
                String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * Retrieves the ID of a user based on their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The ID of the user, or -1 if the user does not exist.
     */
    public static int getUserId(String username, String password) {
        Connection connection = DatabaseManager.getConnection();
        if (connection != null) {
            try {
                String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch (SQLException e) {
                logger.error("An error occurred while retrieving user ID", e);
            }
        }
        return -1; // Return -1 or another invalid value to indicate failure
    }   /**
     * Updates the resources for a user in the database.
     *
     * @param userId The ID of the user.
     * @param silverAmount The new amount of silver.
     * @param ironAmount The new amount of iron.
     */
    public static void updateResources(int userId, int silverAmount, int ironAmount) {
        String sql = "MERGE INTO user_resources KEY(user_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, silverAmount);
            pstmt.setInt(3, ironAmount);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("An error occurred while updating resources", e);
        }
    }

}