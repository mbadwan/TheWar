package com.thewar.utils;
/**
 * Manages user sessions for the game.
 * This class is responsible for tracking and managing the current user's session state,
 * including the user's identification throughout the session.
 */
public class SessionManager {
    /**
     * The ID of the current user in session.
     */
    private static int currentUserId = -1;
    /**
     * Retrieves the current user's ID.
     *
     * @return The ID of the current user. Returns -1 if no user is currently set.
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }
    /**
     * Sets the current user's ID for the session.
     *
     * @param userId The ID of the user to set as the current user.
     */
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
}