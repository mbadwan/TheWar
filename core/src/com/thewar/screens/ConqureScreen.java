
package com.thewar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.logic.*;
import com.thewar.theGame;
import com.thewar.utils.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.thewar.theGame.Difficulty.HARD;
import static com.thewar.theGame.Difficulty.MEDIUM;
import static com.thewar.theGame.numberOfPlayerTanks;
/**
 * The ConqureScreen class represents the game screen where the main gameplay occurs.
 * It manages the game's buildings, player tanks, and projectiles, and handles the game's logic and rendering.
 */
public class ConqureScreen implements Screen, InteractiveScreen,TankDestructionListener {
    private final theGame game;
    private final Viewport viewport;
    public OrthographicCamera camera;
    private UIManager uiManager;
    public static List<Building> buildings = new ArrayList<Building>();

    private Texture baseIcon; // Add this line
    private final Texture abilityBuildingIcon; // Add this line
    private final Texture tankBuildingIcon; // Add this line
    private ShapeRenderer shapeRenderer;

    private Texture BuildingsScreenBacground;
    GameResources resources;
    TankDestructionListener listener;
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    GameResources gameResources;
    float damageModifier = 1;// Default damage modifier for MEDIUM difficulty
    float healthModifier = 1; // Default health modifier for MEDIUM difficulty
    float startingX = 100; // Initial x position for the first tank
    float startingY = 100; // Y position for tanks, assuming it's constant
    GameScreen g;
    public final List<Tank> playerTanks = new ArrayList<>();
    private final Vector2 destination = new Vector2();

    /**
     * Constructs a ConqureScreen with the specified game and resources.
     * Initializes the game's UI, buildings, tanks, and sets up the game's difficulty modifiers.
     *
     * @param game      The main game object.
     * @param resources The game resources object.
     */
    public ConqureScreen(theGame game,GameResources resources) {
        this.game = game;
        this.gameResources = resources;
        // Initialize the camera and viewport
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        BuildingsScreenBacground = new Texture("ConqureBackground.png");
        uiManager = new UIManager(this.gameResources,game.batch);
        this.baseIcon = game.getAssetManager().get(AssetDescriptors.BASE_ICON);
        this.abilityBuildingIcon = game.getAssetManager().get(AssetDescriptors.ABILITY_BUILDING_ICON);
        this.tankBuildingIcon = game.getAssetManager().get(AssetDescriptors.TANK_BUILDING_ICON);
        AssetsManager assetManager = game.getAssetManager();
        Texture tankTexture = assetManager.get(AssetDescriptors.TANK_UP);
        for (int i = 0; i < numberOfPlayerTanks; i++) {
            // Initialize each tank and add to playerTanks list
            Tank tank = new Tank(new Sprite(tankTexture), startingX + i * 300, startingY,this);
            playerTanks.add(tank);
        }
        this.gameResources = gameResources;
        // Initialize the first base
        shapeRenderer = new ShapeRenderer();
        Base base1 = new Base(new Sprite(baseIcon), 100, 426, 400);

        Base base = new Base(new Sprite(baseIcon), 100, 426, 600);
        AbilityBuilding abilityBuilding = new AbilityBuilding(new Sprite(abilityBuildingIcon), 1, "AbilityBuilding", 10, 10, 1280, 600);
        TankBuilding tankBuilding = new TankBuilding(new Sprite(tankBuildingIcon), 1, 200, 600,100,100);

addBuilding(base1);
        addBuilding(base);
        addBuilding(tankBuilding);
        addBuilding(abilityBuilding);


        switch (theGame.GameConfig.currentDifficulty) {
            case EASY:
                damageModifier = 0.75f; // Reduce damage by 25%
                healthModifier = 0.75f; // Reduce damage by 25%
                break;
            case MEDIUM:
                damageModifier = 1; // No change in damage
                healthModifier = 1; // No change in health
                break;
            case HARD:
                damageModifier = 1.25f; // Increase damage by 25%
                healthModifier = 1.25f; // Increase health by 25%
                break;
        }

        for (Building building : buildings) {
            building.setHealth ((int) (building.getHealth()*healthModifier));
            System.out.println("healthmodifier: " + healthModifier);
            System.out.println("damagemodifier: " + damageModifier);
            System.out.println("Building health: " + building.getHealth());

        }
    }
    /**
     * Adds a building to the game's list of buildings.
     * This method is responsible for adding a new building instance to the game's list of buildings.
     * @param building The building to be added to the list.
     */    public void addBuilding(Building building) {
        buildings.add(building);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new MouseInputProcessor(this));    }

    @Override
    public void render(float delta) {


            update(delta);
        maintainDistanceBetweenTanks(100) ;
        camera.update();
            viewport.apply();
            game.batch.setProjectionMatrix(camera.combined);
        for (Tank tank : playerTanks) {
            tank.moveToDestination(Gdx.graphics.getDeltaTime());
        }

            game.batch.begin();
            game.batch.draw(BuildingsScreenBacground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


            for (Building building : buildings) {
                building.draw(game.batch);
            }
// Drawing projectiles
            for (Projectile projectile : projectiles) {
                projectile.draw(game.batch);
            }
        for (Tank tank : playerTanks) {
            tank.draw(game.batch);
        }
uiManager.updateResourcesDisplay(game.batch);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {viewport.update(width, height, true);
        // This method will be called when the screen size changes.
    }

    @Override
    public void pause() {
        // This method will be called when the application is paused.
    }

    @Override
    public void resume() {
        // This method will be called when the application is resumed from a paused state.
    }
    /**
     * Updates the game logic.
     * This method is responsible for updating the state of buildings and projectiles,
     * checking for game over conditions, and transitioning to other screens based on game state.
     * @param deltaTime The time in seconds since the last update.
     */
    public void update(float deltaTime) {
        buildings.forEach(building -> building.update(deltaTime, this));

        List<Projectile> projectilesToRemove = applyDamageAndCollectRemovals(deltaTime);
        projectiles.removeAll(projectilesToRemove);

        // Check if all buildings are destroyed
        boolean allBuildingsDestroyed = buildings.stream().allMatch(Building::isDestroyed);

        if (allBuildingsDestroyed) {
            game.setScreen(new WinningScreen(game,gameResources));
        }
        // Check if all player tanks are destroyed
        if (playerTanks.isEmpty()) {
            game.setScreen(new GameOverScreen(game,gameResources)); // Assuming GameOverScreen exists
        }

    }
    @Override
    public void hide() {
        // This method will be called when this screen is no longer the current screen for a Game.
    }
    @Override
    public Viewport getViewport() {
        return this.viewport; // Assuming `viewport` is a field in `ConqureScreen`
    }

    @Override
    public void dispose() {
uiManager.dispose();    }
    /**
     * Gets the list of projectiles currently active in the game.
     * @return A list of projectiles.
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }
    public Texture getProjectileTexture() {
        return game.getAssetManager().get(AssetDescriptors.PROJECTILES_ICON);
    }
    /**
     * Creates a projectile at a specified screen position.
     * This method is responsible for creating a new projectile instance at the given screen position
     * and adding it to the list of projectiles. */
    public void createProjectileAt(int screenX, int screenY) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        // Example: Create a projectile for each player tank
        for (Tank tank : playerTanks) {
            Projectile newProjectile = new Projectile(getProjectileTexture(), tank.getPosition().x, tank.getPosition().y, worldClick.x, worldClick.y, true); // true for player
            projectiles.add(newProjectile);
        }
    }
    /**
     * Sets the destination for all player tanks.
     * This method updates the destination for all player tanks to the specified coordinates.
     *
     * @param x The x-coordinate of the destination.
     * @param y The y-coordinate of the destination.
     */
    @Override
    public void setDestination(float x, float y) {
        this.destination.set(x, y);
        for (Tank tank : playerTanks) {
            tank.setDestination(x, y);
        }
    }

    /**
     * Maintains a minimum distance between all player tanks.
     * This method adjusts the positions of player tanks to ensure that they maintain a minimum distance from each other.
     *
     * @param minimumDistance The minimum distance that should be maintained between any two tanks.
     */

    private void maintainDistanceBetweenTanks(float minimumDistance) {
        for (int i = 0; i < playerTanks.size(); i++) {
            Tank currentTank = playerTanks.get(i);
            for (int j = i + 1; j < playerTanks.size(); j++) {
                Tank otherTank = playerTanks.get(j);
                float distance = currentTank.distanceTo(otherTank);
                if (distance < minimumDistance) {
                    // Calculate the vector pointing from otherTank to currentTank
                    Vector2 direction = new Vector2(currentTank.position).sub(otherTank.position).nor();
                    // Calculate the adjustment needed
                    float adjustment = (minimumDistance - distance) / 2; // Divide by 2 to adjust both tanks equally
                    // Adjust positions
                    currentTank.position.add(direction.scl(adjustment));
                    otherTank.position.sub(direction.scl(adjustment));
                }
            }
        }
    }
    /**
     * Handles the event when a tank is destroyed.
     * This method removes the destroyed tank from the list of player tanks.
     *
     * @param tank The tank that was destroyed.
     */
    @Override
    public void onTankDestroyed(Tank tank) {
        playerTanks.remove(tank);

    }
    /**
     * Applies damage to targets and collects projectiles that should be removed.
     * This method updates projectiles, applies damage to targets they hit, and collects projectiles that should be removed either because they hit a target or expired.
     *
     * @param deltaTime The time in seconds since the last update.
     * @return A list of projectiles that should be removed.
     */
    private List<Projectile> applyDamageAndCollectRemovals(float deltaTime) {
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile projectile : projectiles) {
            projectile.update(deltaTime, viewport.getWorldWidth(), viewport.getWorldHeight());
            if (projectile.shouldBeRemoved()) {
                toRemove.add(projectile);
            } else {
                // Check for collisions and apply damage here
                // If a collision occurs or the projectile expires, add it to the removal list
                if (checkAndApplyCollision(projectile)) {
                    toRemove.add(projectile);
                }
            }
        }
        return toRemove;
    }/**
     * Checks for collisions between a projectile and targets, applying damage if necessary.
     * This method determines if a projectile collides with any target and applies damage accordingly. It also marks the projectile for removal if it collides or expires.
     *
     * @param projectile The projectile to check for collisions.
     * @return True if the projectile collided with a target or expired, false otherwise.
     */
    private boolean checkAndApplyCollision(Projectile projectile) {
        if (projectile.shouldBeRemoved()) {
            return true; // The projectile is expired and should be removed
        }

        // Check for collisions and apply damage if necessary
        if (projectile.isPlayerProjectile()) {
            for (Building aiTank : buildings) {
                if (projectile.collidesWith(aiTank)) {
                    aiTank.takeDamage(projectile.getDamage(),gameResources); // Apply damage to the AI tank
                    return true; // The projectile hit a target and should be removed
                }
            }
        } else {
            for (Tank playerTank : playerTanks) {
                if (projectile.collidesWith(playerTank)) {
                    playerTank.takeDamage(projectile.getDamage()); // Apply damage to the player tank
                    return true; // The projectile hit a target and should be removed
                }
            }
        }

        return false; // No collision detected, do not remove the projectile
    }
}
