package com.thewar.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.thewar.screens.GameScreen;

import java.util.Random;

/**
 * Represents an AI-controlled tank in the game.
 * This tank automatically moves and attempts to shoot at the player's tank.
 */
public class AITank extends Tank {
    private long lastDirectionChangeTime;
    private final float minX;
    private final float maxX;
    private final float startHeight;
    private final Random random = new Random();
    private final float shootingProbability = 0.01f; // 5% chance to shoot each update
    private int health;
    /**
     * Constructs an AITank with specified parameters.
     *
     * @param sprite       The sprite representing the tank.
     * @param x            The initial x-coordinate of the tank.
     * @param y            The initial y-coordinate of the tank.
     * @param minX         The minimum x-coordinate the tank can move to.
     * @param screenWidth  The width of the screen, used to determine the maximum x-coordinate.
     * @param screenHeight The height of the screen, used to determine the starting height.
     * @param listener     The listener to notify when the tank is destroyed.
     * @param gameScreen   The game screen this tank is part of.
     */
    public AITank(Sprite sprite, float x, float y, float minX, float screenWidth, float screenHeight, TankDestructionListener listener , GameScreen gameScreen) {
        super(sprite, x, y, listener);
        this.gameScreen = gameScreen;
        this.minX = minX;
        this.maxX = screenWidth;
        this.health = 100;
        this.startHeight = screenHeight * 0.75f;
        this.lastDirectionChangeTime = TimeUtils.millis();
        this.position.y = startHeight;

        setRandomHorizontalDestination();
    }
    /**
     * Sets a random horizontal destination for the tank within the allowed range.
     */
    private void setRandomHorizontalDestination() {
        if (this.position.y <= startHeight) {
            float randomX = minX + random.nextFloat() * (maxX - minX);
            setDestination(randomX, this.position.y);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Log before attempting to shoot
        if (TimeUtils.timeSinceMillis(lastDirectionChangeTime) > 2000 && this.position.y <= startHeight) {
            setRandomHorizontalDestination();
            lastDirectionChangeTime = TimeUtils.millis();
        }
        moveToDestination(delta);
        // Call to attempt shooting
        randomShootingAttempt();
    }


    /**
     * Creates a projectile aimed at the specified coordinates.
     *
     * @param x The x-coordinate of the target.
     * @param y The y-coordinate of the target.
     */
    private void createProjectileAt(float x, float y) {
        // Calculate the direction vector from the AI tank's position to the target position
        Vector2 startPosition = new Vector2(this.position.x, this.position.y);
        Vector2 targetPosition = new Vector2(x, y);
        Vector2 direction = targetPosition.sub(startPosition).nor();

        // Calculate the target position for the projectile based on the direction
        // Assuming a fixed distance the projectile will travel
        float distance = 100;
        float targetX = startPosition.x + direction.x * distance;
        float targetY = startPosition.y + direction.y * distance;

        // Create a new Projectile instance
        Projectile projectile = new Projectile(gameScreen.getProjectileTexture(), startPosition.x, startPosition.y, targetX, targetY, false); // false for AI

        // Add the projectile to the game's list of projectiles
        gameScreen.getProjectiles().add(projectile);
    }
    /**
     * Attempts to shoot at the player's tank with a certain probability.
     */
    private void randomShootingAttempt() {

        if (random.nextFloat() < shootingProbability) {

            Vector2 playerPosition = gameScreen.getPlayerTank().getPosition();
            createProjectileAt(playerPosition.x, playerPosition.y);

        }
    }
    public void takeDamage(float damage) {
        this.health -= damage;
        System.out.println("ai Tank took " + damage + " damage. Remaining health: " + this.health);

        if (this.health <= 0) {
            System.out.println("Tank destroyed");
            gameScreen.removeTank(this);
        }

    }


}