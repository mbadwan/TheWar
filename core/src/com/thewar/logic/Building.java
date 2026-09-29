package com.thewar.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.math.Vector2;
import com.thewar.screens.ConqureScreen;

/**
 * Represents a building in the game. This is a base class for all buildings.
 */
public class Building {
    private String type;
    private int level;


    private int silverRequired=50;
    private static int ironRequired=100;
    private int health;
    private final Vector2 position;
    private float height;
    private final Sprite sprite;


    /**
     * Constructs a Building with specified parameters.
     *
     * @param sprite The sprite representing the building.
     * @param level  The level of the building.
     * @param x      The x-coordinate of the building's position.
     * @param y      The y-coordinate of the building's position.
     */
    public Building(Sprite sprite, int level, float x, float y) {

        this.level = level;
        this.sprite = sprite; // Assigning the sprite

        this.health = 100;
        this.position = new Vector2(x, y); // Initialize position with the provided coordinates
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getSilverRequired() {
        return silverRequired;
    }

    public static int getIronRequired() {
        return ironRequired;
    }




public void update(float delta,ConqureScreen conquerScreen) {
    randomShootingAttempt(conquerScreen);
}
    /**
     * Applies damage to the building and checks if it is destroyed.
     * If the building is destroyed, it adds resources to the game's resources pool.
     *
     * @param damage    The amount of damage to apply to the building.
     * @param resources The game's resources pool, to which resources are added if the building is destroyed.
     */
    public void takeDamage(int damage, GameResources resources) {
        this.health -= damage;
        if (this.health <= 0) {
            System.out.println("Building destroyed");
            resources.addIron(200);
            resources.addSilver(200);
                ConqureScreen.buildings.remove(this);}
    }
    /**
     * Checks if the building is destroyed.
     *
     * @return true if the building's health is 0 or less, false otherwise.
     */
    public boolean isDestroyed() {
        return health <= 0;
    }
    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Attempts to shoot at player tanks with a certain probability each update.
     * This method simulates the building's ability to attack.
     *
     * @param conquerScreen The game screen context, used for accessing player tanks and adding projectiles.
     */
    public void randomShootingAttempt(ConqureScreen conquerScreen) {
        // Example: 1% chance to shoot each update
        float shootingProbability = 0.01f; // Adjusted to 1%
        if (Math.random() < shootingProbability) {
            for (Tank tank : conquerScreen.playerTanks) {
                Vector2 tankPosition = tank.getPosition();
                createProjectileAt(tankPosition.x, tankPosition.y, conquerScreen);
            }
        }
    }
    /**
     * Creates a projectile aimed at the specified coordinates.
     * This method is used by {@link #randomShootingAttempt(ConqureScreen)} to simulate attacking player tanks.
     *
     * @param x            The x-coordinate of the target.
     * @param y            The y-coordinate of the target.
     * @param conquerScreen The game screen context, used for adding the projectile to the game.
     */
    private void createProjectileAt(float x, float y, ConqureScreen conquerScreen) {
        Vector2 startPosition = new Vector2(this.position.x, this.position.y);
        Vector2 targetPosition = new Vector2(x, y);
        Vector2 direction = targetPosition.sub(startPosition).nor();

        float distance = 100; //
        float targetX = startPosition.x + direction.x * distance;
        float targetY = startPosition.y + direction.y * distance;

        Projectile projectile = new Projectile(conquerScreen.getProjectileTexture(), startPosition.x, startPosition.y, targetX, targetY, false); // false for enemy
        conquerScreen.getProjectiles().add(projectile);
    }


        // Draw the building sprite at the position (x, y)
        public void draw(Batch batch) {
            // Adjust the drawing position so the sprite is drawn with its center at the position
            sprite.setPosition(position.x - sprite.getWidth() * sprite.getScaleX() / 2, position.y - sprite.getHeight() * sprite.getScaleY() / 2);
            sprite.draw(batch);
        }
    private boolean isSelected = false; // Step 1: Track selection state

    // Step 2: Method to select the building
    public void selectBuilding() {
        this.isSelected = true;
    }

    // Step 3: Method to deselect the building
    public void deselectBuilding() {
        this.isSelected = false;
    }

    // Step 4: Check if the building is selected
    public boolean isBuildingSelected() {
        return isSelected;
    }
    public void setHealth(int buildingHealth) {
        this.health = buildingHealth;
    }
    public int getHealth() {
        return health;
    }

    public float getWidth() {
        return sprite.getWidth() ;
    }
}
