package com.thewar.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
/**
 * Represents a base building in the game. A base is responsible for generating
 * resources and maintaining its health status.
 */
public class Base extends Building{
    private int health;

    private int rawIron;
    private int rawSilver;
    private Vector2 position;
    /**
     * Constructs a new Base instance with specified sprite, health, and position.
     *
     * @param sprite The sprite representing the base.
     * @param health The initial health of the base.
     * @param x The x-coordinate of the base's position.
     * @param y The y-coordinate of the base's position.
     */
    public Base(Sprite sprite, int health, float x, float y) {
        super(sprite, 1, x, y);
        this.health = health;
        this.rawIron = 0;
        this.rawSilver = 0;
        this.position = new Vector2(x, y);

    }

    // Getters and setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRawIron() {
        return rawIron;
    }

    public void setRawIron(int rawIron) {
        this.rawIron = rawIron;
    }

    public int getRawSilver() {
        return rawSilver;
    }

    public void setRawSilver(int rawSilver) {
        this.rawSilver = rawSilver;
    }


    public Vector2 getPosition() { return new Vector2(position);
    }
    /**
     * Sets the position of the base.
     *
     * @param x The x-coordinate of the new position.
     * @param y The y-coordinate of the new position.
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }
    /**
     * Simulates the generation of resources by the base. This method increments
     * the raw iron and raw silver by predefined amounts.
     */
    public void generateResources() {
        // Increment rawIron and rawSilver by some amount
        this.rawIron += 2;
        this.rawSilver += 1;
    }
}