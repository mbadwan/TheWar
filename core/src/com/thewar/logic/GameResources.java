package com.thewar.logic;

/**
 * Manages the game's resources including iron and silver.
 * This class provides methods to add, subtract, and retrieve the current amounts of resources.
 */
public class GameResources {
    private int iron;
    private int silver;
    private int rawIron;
private int rawSilver;
    /**
     * Constructs a new GameResources instance with initial values for iron and silver.
     *
     * @param initialIron Initial amount of iron.
     * @param initialSilver Initial amount of silver.
     */
    public GameResources(int initialIron, int initialSilver) {
        this.iron = initialIron;
        this.silver = initialSilver;
    }

    /**
     * Adds iron to the current total.
     *
     * @param amount The amount of iron to add.
     */
    public void addIron(int amount) {
        iron += amount;
    }

    public void addSilver(int amount) {
        silver += amount;
    }

    // Getters and setters
    public int getIron() {
        return iron;
    }
    public void setIron(int iron) {
        this.iron = iron;
    }
    public int getSilver() {
        return silver;
    }
    public void setSilver(int silver) {
        this.silver = silver;
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


}