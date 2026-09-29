package com.thewar.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
/**
 * Represents a projectile in the game, which can be fired by either the player or AI units.
 * It handles the movement, rendering, and collision detection of the projectile.
 */
public class Projectile {
    private final Vector2 position;
    private final boolean isPlayerProjectile; // True if from player, false if from AI

    private final Vector2 velocity;
    private final Texture texture;
    private final float speed = 1500; // Define the speed of the projectile
    private boolean shouldBeRemoved = false; // Flag to indicate removal
    private float width; // Added width field
    private float height; // Added height field
    private float damage    = 10; // Added damage field
    /**
     * Constructs a new Projectile instance.
     *
     * @param texture The texture for the projectile.
     * @param startX The starting X coordinate.
     * @param startY The starting Y coordinate.
     * @param targetX The target X coordinate.
     * @param targetY The target Y coordinate.
     * @param isPlayerProjectile True if the projectile is fired by the player, false if by AI.
     */
    public Projectile(Texture texture, float startX, float startY, float targetX, float targetY,boolean isPlayerProjectile) {
        this.texture = texture;
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(targetX - startX, targetY - startY).nor().scl(speed);
        this.width = texture.getWidth(); // Initialize width based on texture size
        this.height = texture.getHeight(); // Initialize height based on texture size
        this.isPlayerProjectile = isPlayerProjectile;

    }
    /**
     * Updates the position of the projectile and checks for out-of-bounds to mark for removal.
     *
     * @param deltaTime The time elapsed since the last update.
     * @param worldWidth The width of the game world.
     * @param worldHeight The height of the game world.
     */
    public void update(float deltaTime, float worldWidth, float worldHeight) {
        // Apply velocity to position
        this.position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Check if the projectile is out of bounds
        if (position.x < 0 || position.x > worldWidth || position.y < 0 || position.y > worldHeight) {
            shouldBeRemoved = true; // Mark this projectile for removal
        }
    }


    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }

    /**
     * Draws the projectile on the screen.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    public void draw(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, position.x, position.y);
        }
    }
    /**
     * Checks if the projectile collides with a given object.
     *
     * @param object The object to check collision against.
     * @return True if there is a collision, false otherwise.
     */
    public boolean collidesWith(Object object) {
        if (object instanceof Tank) {
            Tank tank = (Tank) object;
            // Collision detection logic for Tank
            return this.position.x < tank.getPosition().x + tank.getWidth() &&
                    this.position.x + this.width > tank.getPosition().x &&
                    this.position.y < tank.getPosition().y + tank.getHeight() &&
                    this.position.y + this.height > tank.getPosition().y;
        } else if (object instanceof Building) {
            Building building = (Building) object;
            // Collision detection logic for Building
            return this.position.x < building.getPosition().x + building.getWidth() &&
                    this.position.x + this.width > building.getPosition().x &&
                    this.position.y < building.getPosition().y + building.getHeight() &&
                    this.position.y + this.height > building.getPosition().y;
        }
        return false;
    }


    /**
     * Returns the position of the projectile.
     *
     * @return The position vector of the projectile.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Checks if the projectile was fired by the player.
     *
     * @return True if the projectile is a player projectile, false if AI.
     */
    public boolean isPlayerProjectile() {
        return isPlayerProjectile;
    }
    /**
     * Marks the projectile for removal from the game world.
     */
    public void markForRemoval() { // Method to mark the projectile for removal
        shouldBeRemoved = true;
    }
    /**
     * Checks if the projectile should be removed from the game world.
     *
     * @return True if the projectile should be removed, false otherwise.
     */
    public int getDamage() {
        return (int) this.damage; // Return the damage value
    }
}
