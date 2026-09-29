package com.thewar.screens;
import static com.thewar.theGame.numberOfPlayerTanks;

import static sun.util.locale.LocaleUtils.isEmpty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.logic.*;
import com.thewar.theGame;
import com.thewar.utils.AssetDescriptors;
import com.thewar.utils.AssetsManager;
import com.thewar.utils.InteractiveScreen;
import com.thewar.utils.MouseInputProcessor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Represents the main game screen where the player interacts with the ai tanks world.
 * This screen handles rendering of the game world, including tanks, projectiles, and the game UI.
 */
public class GameScreen implements Screen , InteractiveScreen , TankDestructionListener{
    private final ShapeRenderer shapeRenderer;
    public  static final float SPEED=500;
    float startingX = 100; // Initial x position for the first tank
    float startingY = 100; // Y position for tanks, assuming it's constant
private UIManager uiManager;
    public final Viewport viewport;
    public OrthographicCamera camera;
    theGame game;
    private List<Tank> tanks = new ArrayList<Tank>();
    private final List<Tank> playerTanks = new ArrayList<>();
    GameResources gameResources;
    float x=0;
    float y=0;
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    private final Texture gameeScreenBacground;
    private List<Tank> tanksToRemove = new ArrayList<Tank>(); // Added declaration for tanksToRemove
    private Music backgroundMusic;
    ConqureScreen g;
    float damageModifier = 1;// Default damage modifier for MEDIUM difficulty
    float healthModifier = 1; // Default health modifier for MEDIUM difficulty

    /**
     * Constructs the GameScreen with the game instance and game resources.
     * This constructor initializes the game screen, setting up the UI manager, loading textures for tanks,
     * creating player and AI tanks, setting up the camera and viewport, and configuring game difficulty modifiers.
     * It also starts playing background music and adjusts AI tank health based on the game difficulty.
     *
     * @param game          The game instance this screen is part of.
     * @param gameResources The game resources to be used in this screen.
     */
    public GameScreen(theGame game,GameResources gameResources) {
        this.game = game;
        this.gameResources = gameResources;
        uiManager = new UIManager(this.gameResources, game.batch);
       System.out.println("GameResources: " + gameResources);
        AssetsManager assetManager = game.getAssetManager(); // Get the asset manager from the game instance
        Texture tankTexture = assetManager.get(AssetDescriptors.TANK_UP);
        for (int i = 0; i < numberOfPlayerTanks; i++) {
            // Initialize each tank and add to playerTanks list
            Tank tank = new Tank(new Sprite(tankTexture), startingX + i * 300, startingY,this   );
            playerTanks.add(tank);
        }
        gameeScreenBacground = new Texture("sky.png");
        shapeRenderer = new ShapeRenderer();
        x = 40;
        y = 30;

        Texture aiTankTexture = assetManager.get(AssetDescriptors.AITANK);
        tanks = new ArrayList<>();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        AITank aiTankTop = new AITank(new Sprite(aiTankTexture), 1920 / 2f, 1080 * 0.75f, 0, 1920, 1080, this,this);
        AITank aiTankBottom = new AITank(new Sprite(aiTankTexture), 1920 / 2f, 1080 * 0.25f, 0, 1920, 1080, this,this);
        tanks.add(aiTankTop);
        tanks.add(aiTankBottom);
             backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("mezhdunami-delusions-141269.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
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

        for (Tank tank : tanks) {
            tank.setHealth ((int) (Tank.getHealth()*healthModifier));
            System.out.println("healthmodifier: " + healthModifier);
            System.out.println("damagemodifier: " + damageModifier);
            System.out.println("Tank health: " + Tank.getHealth());

        }
        viewport.apply();
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
    }
    /**
     * Sets the game's input processor to handle mouse input.
     * This method is called when the screen becomes visible.
     */
    @Override
    public void show() {

        Gdx.input.setInputProcessor(new MouseInputProcessor(this));
    }

    private final Vector2 destination = new Vector2();
    /**
     * Sets the destination for all player-controlled tanks.
     * This method updates the destination point and instructs each tank to move towards it.
     *
     * @param x The x-coordinate of the destination point.
     * @param y The y-coordinate of the destination point.
     */
    public void setDestination(float x, float y) {
        this.destination.set(x, y);
        for (Tank tank : playerTanks) {
            tank.setDestination(x, y);
        }
    }

    /**
     * Returns the viewport associated with this screen.
     * The viewport is used to manage how the game's camera maps the scene to the screen.
     *
     * @return The current viewport instance.
     */
    @Override
    public Viewport getViewport() {
        return this.viewport; // Return the viewport instance
    }
    /**
     * Renders the game screen each frame, updating game objects and drawing them to the screen.
     * This method is responsible for updating the game's state, clearing the screen,
     * drawing all game elements (tanks, projectiles, UI), and maintaining the game's visual consistency.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Update the camera and the viewport
        camera.update();
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        for (Tank tank : playerTanks) {
            tank.moveToDestination(Gdx.graphics.getDeltaTime());
        }
        // Update the game state
        update(delta);
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        maintainDistanceBetweenTanks(100) ;
        // Start drawing
        game.batch.begin();
        // Draw the background
        game.batch.draw(gameeScreenBacground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Draw the tank at its current position
        for (Tank tank : playerTanks) {
            tank.draw(game.batch);
        }

        // Draw AI Tanks
        for (Tank tank : tanks) {
            tank.draw(game.batch);
        }

        // Drawing projectiles
        for (Projectile projectile : projectiles) {
            System.out.println("Drawing projectile at position: " + projectile.getPosition()); // Logging
            projectile.draw(game.batch);
        }
        uiManager.updateResourcesDisplay(game.batch);

        // End drawing
        game.batch.end();

        // Ensure the projection matrix is set correctly for the shapeRenderer
        shapeRenderer.setProjectionMatrix(camera.combined);
        // Begin drawing shapes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

// Set the color to red for the rectangle
        shapeRenderer.setColor(1, 0, 0, 1);

// Draw the red rectangle to match the playfield dimensions and position
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

// End drawing
        shapeRenderer.end();
    }
    /**
     * Updates the game state each frame, including checking for AI and player tank destruction,
     * handling projectile collisions, and navigating to the victory or game over screen as appropriate.
     * This method is a critical part of the game loop, ensuring that game logic is processed,
     * such as moving tanks, checking for collisions, and updating the game world based on player actions and AI behavior.
     *
     * @param delta The time in seconds since the last frame. Used to ensure smooth and consistent updates.
     */
    public void update(float delta) {
        boolean allAITanksDestroyed = true;
        for (Tank tank : tanks) {
            if (!tank.isDestroyed()) {
                allAITanksDestroyed = false;
                break;
            }
        }

        // Navigate to ConqureScreen if all AI tanks are destroyed
        if (allAITanksDestroyed) {
            game.setScreen(new ConqureScreen(this.game, getGameResources()));
        }

        List<Projectile> toRemove = new ArrayList<>();
// Check for collisions between AI projectiles and player tanks
        for (Projectile projectile : projectiles) {
            if (!projectile.isPlayerProjectile()) {
                for (Tank playerTank : playerTanks) {
                    if (projectile.collidesWith(playerTank)) {
                        playerTank.takeDamage( projectile.getDamage() * damageModifier) ;
                        projectile.markForRemoval();
                        break; // Stop checking after the first collision to avoid double counting
                    }
                }
            }
        }
        // Check and remove destroyed player tanks

        for (Tank tank : playerTanks) {
            if (tank.isDestroyed()) {
                tanksToRemove.add(tank);
            }
        }
        playerTanks.removeAll(tanksToRemove);
        tanksToRemove.clear(); // Clear the list for reuse

// Update all player tanks
        for (Tank playerTank : playerTanks) {
            playerTank.update(delta);
        }
        // Update all player tanks
        for (Tank playerTank : tanks) {
            playerTank.update(delta);
        }


// Check for collisions between player projectiles and AI tanks
        for (Projectile projectile : projectiles) {
            projectile.update(delta, viewport.getWorldWidth(), viewport.getWorldHeight());
            if (projectile.isPlayerProjectile()) {
                for (Tank aiTank : tanks) {
                    if (projectile.collidesWith(aiTank)) {
                        ((AITank) aiTank).takeDamage(projectile.getDamage()); // Assuming getDamage method exists
                        toRemove.add(projectile);
                        break; // Break to avoid checking other tanks for this projectile
                    }
                }
            }
            if (projectile.shouldBeRemoved()) {
                toRemove.add(projectile);
            }
        }

// Remove all marked projectiles
        projectiles.removeAll(toRemove);

// Remove destroyed tanks
        removeDestroyedTanks();

// Check if all player tanks are destroyed
        checkPlayerHealth();


    }
    /**
     * Adjusts the game screen size and repositions the camera based on the new dimensions.
     * This method is called whenever the window size changes, ensuring that the game's viewport
     * and camera are updated to match the new size, maintaining the game's aspect ratio and visibility.
     *
     * @param width  The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void resize(int width, int height) {
        // Update the viewport to the new screen size
        viewport.update(width, height, true);
        // Recenter the camera on the game world or UI
        System.out.println("Screen Widthres: " + viewport.getWorldWidth() + ", Screen Heightres: " + viewport.getWorldHeight());
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        viewport.apply();
        camera.update();

}
    /**
     * Pauses the game. This method is called when the game window loses focus or when the game is otherwise paused.
     * Override this method to pause game logic or animations.
     */
    @Override
    public void pause() {

    }
    /**
     * Retrieves the list of projectiles currently active in the game.
     * This method allows access to the projectiles for collision detection, rendering, and other logic.
     *
     * @return A list of {@link Projectile} objects representing the active projectiles.
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }
    public Texture getProjectileTexture() {
        return game.getAssetManager().get(AssetDescriptors.PROJECTILES_ICON);
    }/**
     * Creates a projectile at a specified screen position. This method is called in response to user input,
     * such as a mouse click or touch event, translating the screen position to world coordinates and
     * creating a projectile from the player's tank towards the target location.
     *
     * @param screenX The x-coordinate of the screen position where the projectile is created.
     * @param screenY The y-coordinate of the screen position where the projectile is created.
     */
    public void createProjectileAt(int screenX, int screenY) {
        Vector2 worldClick = viewport.unproject(new Vector2(screenX, screenY));
        for (Tank tank : playerTanks) {
            Projectile newProjectile = new Projectile(getProjectileTexture(), tank.getPosition().x, tank.getPosition().y, worldClick.x, worldClick.y, true); // true for player
            projectiles.add(newProjectile);
        }
    }
    @Override
    public void resume() {

    }

    @Override
    public void hide() {        backgroundMusic.stop();


    }

    @Override
    public void dispose() {
        uiManager.dispose();

        backgroundMusic.dispose();


    }
    /**
     * Removes a specified tank from the game by adding it to a list of tanks to be removed.
     * This method should be called when a tank is destroyed, marking it for removal from the game.
     * Actual removal from the game world occurs in the {@link #removeDestroyedTanks()} method.
     *
     * @param tank The tank to be removed.
     */
    public void removeTank(Tank tank) {
        // This method will be called to remove a tank from the game
        tanksToRemove.add(tank);
    }
    /**
     * Removes all tanks that have been marked for removal from the game world.
     * This method should be called at the end of the game's update cycle to safely remove tanks.
     * It ensures that tanks are not modified or removed while iterating over them during the game update.
     */
    private void removeDestroyedTanks() {
        tanks.removeAll(tanksToRemove);
        tanksToRemove.clear();
    }
    /**
     * Retrieves the first player-controlled tank in the game.
     * This method is useful for operations that need to reference a single player tank, such as camera following.
     *
     * @return The first tank in the list of player tanks, or null if there are no player tanks.
     */
    public Tank getPlayerTank() {
        if (!playerTanks.isEmpty()) {
            return playerTanks.get(0); // Return the first tank in the list
        }
        return null; // Return null if the list is empty
    }
    /**
     * Checks the health of all player tanks and transitions to the GameOverScreen if all are destroyed.
     * This method is a critical part of the game's logic to determine the end of the game.
     */
    public void checkPlayerHealth() {
        boolean allTanksDestroyed = true;
        for (Tank tank : playerTanks) {
            if (!tank.isDestroyed()) {
                allTanksDestroyed = false;
                break;
            }
        }
        if (allTanksDestroyed) {
            game.setScreen(new GameOverScreen(game,getGameResources()));
        }
    }/**
     * Ensures that player tanks maintain a minimum distance from each other to avoid overlapping.
     * This method iterates through all player tanks and adjusts their positions if they are too close to each other.
     *
     * @param minimumDistance The minimum allowed distance between any two tanks.
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
     * Callback method to remove a tank from the player tanks list when it is destroyed.
     * This method is part of the TankDestructionListener interface, allowing for decoupled logic handling tank destruction.
     *
     * @param tank The tank that was destroyed.
     */
    @Override
    public void onTankDestroyed(Tank tank) {
        playerTanks.remove(tank);

    }/**
     * Provides access to the game resources associated with this screen.
     * This method allows other parts of the game to access resources like textures and sounds without direct coupling.
     *
     * @return The GameResources instance containing game assets and configurations.
     */
    public GameResources getGameResources() {
        return this.gameResources;
    }
}