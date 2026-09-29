package com.thewar.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
/**
 * Represents a building that can grant abilities to tanks.
 * This building requires resources to add abilities to tanks.
 */
public class AbilityBuilding extends Building {
    private String abilityType;
    private static int ironRequired;
    private int silverRequired;
    private Tank tank;
    /**
     * Constructs an AbilityBuilding with specified parameters.
     *
     * @param sprite        The sprite representing the building.
     * @param level         The level of the building.
     * @param abilityType   The type of ability this building can grant.
     * @param ironRequired  The amount of iron required to grant the ability.
     * @param silverRequired The amount of silver required to grant the ability.
     * @param x             The x-coordinate of the building's position.
     * @param y             The y-coordinate of the building's position.
     */
    public AbilityBuilding(Sprite sprite,int level, String abilityType, int ironRequired, int silverRequired,  float x, float y) {
        super(sprite, level, x, y);

        this.abilityType = abilityType;
        this.ironRequired = ironRequired;
        this.silverRequired = silverRequired;
        this.tank = tank;
    }

    /**
     * Gets the type of ability this building can grant.
     *
     * @return The ability type.
     */    public String getAbilityType() {
        return abilityType;
    }
    /**
     * Sets the type of ability this building can grant.
     *
     * @param abilityType The ability type.
     */
    public void setAbilityType(String abilityType) {
        this.abilityType = abilityType;
    }

    public static int getIronRequired() {
        return ironRequired;
    }

    public void setIronRequired(int ironRequired) {
        AbilityBuilding.ironRequired = ironRequired;
    }

    public int getSilverRequired() {
        return silverRequired;
    }

    public void setSilverRequired(int silverRequired) {
        this.silverRequired = silverRequired;
    }
    /**
     * Attempts to add the ability to a tank, consuming resources.
     *
     * @return true if the ability was successfully added, false otherwise.
     */
    public boolean addAbilityToTank() {
        if (ironRequired > 0 && silverRequired > 0) {

            ironRequired--;
            silverRequired--;
            return true;
        } else {
            System.out.println("Not enough iron or silver to add the ability to the tank.");
            return false;
        }
    }


}