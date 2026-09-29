package com.thewar.utils;

import com.badlogic.gdx.utils.viewport.Viewport;
/**
 * Defines the behavior for interactive game screens.
 * This interface outlines the methods required for screens that interact with user input,
 * such as creating projectiles and setting destinations for entities.
 */
public interface InteractiveScreen {
    /**
     * Creates a projectile at the specified screen coordinates.
     * This method is responsible for creating and initializing a projectile entity at the given location.
     *
     * @param screenX The x-coordinate on the screen where the projectile should be created.
     * @param screenY The y-coordinate on the screen where the projectile should be created.
     */
    void createProjectileAt(int screenX, int screenY);
    /**
     * Sets the destination for an entity.
     * This method updates the target destination to the specified coordinates.
     *
     * @param x The x-coordinate of the destination.
     * @param y The y-coordinate of the destination.
     */
    void setDestination(float x, float y);

    /**
     * Retrieves the viewport associated with this screen.
     * The viewport defines the area of the world that is visible in this screen.
     *
     * @return The viewport associated with this screen.
     */
    Viewport getViewport(); // Add this method

}