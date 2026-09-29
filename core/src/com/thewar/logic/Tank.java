package com.thewar.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.screens.ConqureScreen;
import com.thewar.screens.GameScreen;
import com.thewar.theGame;
/**
 * Represents a tank in the game, providing the base functionality for both player and AI tanks.
 * This class handles movement, health, and damage mechanics for a tank.
 */
public class Tank {
    private TankDestructionListener destructionListener;

    GameScreen gameScreen;
    private final Sprite sprite;
    public final Vector2 position;
    private float rotation;
    private static float health;
    public Viewport viewport;
    public OrthographicCamera camera;
    public static final float SPEED = 200;
    private final float speed = SPEED;
    private final Vector2 destination = new Vector2(); // Declare the destination variable
    private Rectangle playableArea;
    private theGame game; // Add a reference to the main game class
    private int damage;
    /**
     * Constructs a Tank with specified parameters.
     *
     * @param sprite    The sprite representing the tank.
     * @param x         The initial x-coordinate of the tank.
     * @param y         The initial y-coordinate of the tank.
     * @param listener  The listener to notify when the tank is destroyed.
     */
    public Tank(Sprite sprite, float x, float y, TankDestructionListener listener) {
        this.sprite = sprite;
        this.rotation = 0;
        this.position = new Vector2(x, y);
        this.health = 50;
        this.damage = 10;
        this.destructionListener = listener;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); // Use ScreenViewport
        this.playableArea = new Rectangle(viewport.getWorldWidth(),  viewport.getWorldHeight(), viewport.getWorldWidth(), viewport.getWorldHeight());
        viewport.apply(true);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
    }
    /**
     * Draws the tank on the screen.
     *
     * @param batch The batch used for drawing.
     */
    public void draw(Batch batch) {
        // Adjust the drawing position so the sprite is drawn with its center at the position
        sprite.setPosition(position.x - sprite.getWidth() * sprite.getScaleX() / 2, position.y - sprite.getHeight() * sprite.getScaleY() / 2);
        sprite.setRotation(rotation);
        sprite.draw(batch);
    }

    /**
     * Updates the tank's position and rotation based on its destination.
     *
     * @param delta The time in seconds since the last update.
     */
    public void update(float delta) {
        // Calculate the angle to the destination
        float angle = calculateRotation(destination);
        // Move the tank towards the destination
        position.x += (float) (Math.cos(angle) * speed * delta);
        position.y += (float) (Math.sin(angle) * speed * delta);
    }
    /**
     * Calculates the rotation angle towards the destination.
     *
     * @param destination The destination vector.
     * @return The angle in radians.
     */
    private float calculateRotation(Vector2 destination) {
        float angle = (float) Math.atan2(destination.y - position.y, destination.x - position.x);
        rotation = (float) Math.toDegrees(angle);
        return angle;
    }
    public Vector2 getPosition() {
        return new Vector2(position);
    }
    public void increaseHealth(int amount) {
        this.health += amount;
    }
    public void increaseDamage(int amount) {
        this.damage += amount;
    }
    /**
     * Sets the destination for the tank.
     * This method updates the tank's target destination to the specified coordinates.
     *
     * @param x The x-coordinate of the destination.
     * @param y The y-coordinate of the destination.
     */
    public void setDestination(float x, float y) {
        // Set the destination for the tank
        this.destination.set(x, y);
    }
    /**
     * Updates the tank's rotation to face its current destination.
     * This method calculates the angle between the tank's current position and its destination,
     * then updates the tank's rotation to face towards the destination.
     */
    private void updateRotationTowardsDestination() {
        float angle = (float) Math.atan2(destination.y - position.y, destination.x - position.x);
        rotation = (float) Math.toDegrees(angle);
    }/**
     * Calculates the distance to another tank.
     * This method returns the distance between this tank and another specified tank.
     *
     * @param otherTank The other tank to calculate the distance to.
     * @return The distance to the other tank.
     */
    public float distanceTo(Tank otherTank) {
        return this.position.dst(otherTank.position);
    }

    /**
     * Moves the tank towards its destination.
     * This method updates the tank's position, moving it towards its destination based on its speed
     * and the elapsed time since the last update. It also updates the tank's rotation to face the destination.
     *
     * @param deltaTime The time in seconds since the last update.
     */
    public void moveToDestination(float deltaTime) {
        if (!position.epsilonEquals(destination, 1.0f)) {
            Vector2 direction = new Vector2(destination.x - position.x, destination.y - position.y).nor();
            position.mulAdd(direction, speed * deltaTime);
            updateRotationTowardsDestination();
        }
    }
    /**
     * Applies damage to the tank and checks if it has been destroyed.
     * This method reduces the tank's health by the specified amount of damage. If the tank's health falls to 0 or below,
     * it is considered destroyed, and the destruction listener (if set) is notified.
     *
     * @param damage The amount of damage to apply to the tank.
     */
    public void takeDamage(float damage) {
        this.health -= damage;
        System.out.println("Player Tank took " + damage + " damage. Remaining health: " + this.health);
        if (this.health <= 0) {
            System.out.println("Player Tank destroyed");
            if (destructionListener != null) {
                destructionListener.onTankDestroyed(this);
            }
        }
    }
    public float getWidth() {
        return sprite.getWidth() * sprite.getScaleX();
    }

    public float getHeight() {
        return sprite.getHeight() * sprite.getScaleY();
    }
    public boolean isDestroyed() {
        return health <= 0;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public static int getHealth() {
        return (int) health;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }
}

