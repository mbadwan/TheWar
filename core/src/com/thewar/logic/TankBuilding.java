package com.thewar.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
/**
 * Represents a building dedicated to producing and enhancing tanks within the game.
 * This building can increase the health of a tank by using iron resources.
 */
public class TankBuilding extends Building {

    private int ironForHealth;
    private Tank tank;
    private int Iron=0;
    private int Silver =0;
    /**
     * Constructs a new TankBuilding with specified parameters.
     *
     * @param sprite The sprite representing the tank building.
     * @param level The level of the tank building.
     * @param x The x-coordinate of the tank building's position.
     * @param y The y-coordinate of the tank building's position.
     * @param Iron The amount of iron resources dedicated to the tank building.
     * @param Silver The amount of silver resources dedicated to the tank building.
     */
    public TankBuilding(Sprite sprite, int level, float x, float y,int Iron,int Silver) {
        super(sprite, level, x, y);



        this.Iron=Iron;
        this.Silver=Silver;

    }



    /**
     * Attempts to add health to the tank by using iron resources.
     * If there is enough iron, the tank's health is increased, and the method returns true.
     * If there is not enough iron, it prints a message and returns false.
     *
     * @return true if the health was successfully added to the tank, false otherwise.
     */
    public boolean addHealthToTank() {
        if (ironForHealth > 0) {
            tank.increaseHealth(1); // Increase the health of the tank
            ironForHealth--;
            return true;
        } else {
            System.out.println("Not enough iron to increase the tank's health.");
            return false;
        }
    }

}