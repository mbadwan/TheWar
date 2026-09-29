
package com.thewar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.logic.*;
import com.thewar.theGame;
import com.thewar.utils.AssetDescriptors;
import com.thewar.utils.AssetsManager;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * The BuildingsScreen class represents the screen where players can manage their buildings,
 * including creating new ones and viewing existing structures. It also allows for resource
 * management and preparation for battles.
 */
public class BuildingsScreen implements Screen {
    private theGame game;
    private Viewport viewport;
    public OrthographicCamera camera;
    private List<Base> bases = new ArrayList<>(); // Ensure bases is initialized to avoid null pointer exceptions
    private List<Building> buildings = new ArrayList<>();
    private Texture baseIcon, abilityBuildingIcon, tankBuildingIcon;
    private Texture BuildingsScreenBackground;
    private Stage stage;
    private Skin skin;
    private UIManager uiManager;
    private GameResources gameResources;
    /**
     * Constructor for BuildingsScreen. Initializes the game state, loads textures,
     * and sets up the UI components.
     * @param game The main game object that allows for screen transitions.
     */
    public BuildingsScreen(theGame game) {
        this.game = game;
        this.gameResources = new GameResources(200, 100);

        baseIcon = game.getAssetManager().get(AssetDescriptors.BASE_ICON);

        Base base = new Base(new Sprite(baseIcon), 100, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        buildings.add(base);
        loadTextures();
        initializeUI();
        scheduleResourceGeneration();
       }
    /**
     * Adds a building to the list of buildings.
     * @param building The building to add.
     */    public void addBuilding(Building building) {
        this.buildings.add(building);
    }
    /**
     * Loads the textures for the buildings and background.
     */
    private void loadTextures() {
        BuildingsScreenBackground = new Texture("BuildingsScreenBacground.png");
        abilityBuildingIcon = game.getAssetManager().get(AssetDescriptors.ABILITY_BUILDING_ICON);
        tankBuildingIcon = game.getAssetManager().get(AssetDescriptors.TANK_BUILDING_ICON);
    }
    /**
     * Initializes the user interface, including setting up the camera, viewport, and stage.
     * Also calls methods to set up various UI components.
     */
    private void initializeUI() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
        setupBuildingButtons();
        setupTankSelectionButtons();
        setupMashButtons();
        uiManager = new UIManager(gameResources, game.batch);
    }

    /**
     * Schedules the generation of resources from bases at regular intervals.
     * This method sets up a timer that periodically updates the game's resources
     * based on the output of each base. It ensures that resources are generated
     * consistently over time, reflecting the ongoing production capabilities of the player's bases.
     */
    private void scheduleResourceGeneration() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                updateGameResourcesFromBases();
                for (Base base : bases) {
                    base.generateResources(); // Call generateResources for each base
                }
            }
        }, 1, 1); // Adjusted to start after 1 second and repeat every second
    }
    /**
     * Checks if an Ability Building exists among the buildings.
     * @return true if an Ability Building exists, false otherwise.
     */
    private boolean hasAbilityBuilding() {
        for (Building building : buildings) {
            if (building instanceof AbilityBuilding) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if a Tank Building exists among the buildings.
     * @return true if a Tank Building exists, false otherwise.
     */
    private boolean hasTankBuilding() {
        for (Building building : buildings) {
            if (building instanceof TankBuilding) {
                return true;
            }
        }
        return false;
    }
    private void updateGameResourcesFromBases() {
        final int RAW_IRON_TO_IRON_RATE = 3;
        final int RAW_SILVER_TO_SILVER_RATE = 2;
        if (hasAbilityBuilding()) {
            for (Base base : bases) {
                int rawIron = base.getRawIron();
                int rawSilver = base.getRawSilver();

                int ironToAdd = rawIron / RAW_IRON_TO_IRON_RATE;
                int silverToAdd = rawSilver / RAW_SILVER_TO_SILVER_RATE;

                // Correctly calculate the remaining raw resources
                int newRawIron = rawIron % RAW_IRON_TO_IRON_RATE;
                int newRawSilver = rawSilver % RAW_SILVER_TO_SILVER_RATE;

                // Update gameResources with the new amounts of iron and silver
                gameResources.addIron(ironToAdd);
                gameResources.addSilver(silverToAdd);

                // Update the Base object with the new raw resource amounts
                base.setRawIron(newRawIron);
                base.setRawSilver(newRawSilver);
            }
        }
        if (hasTankBuilding()) {
            for (Base base : bases) {
                int rawIron = base.getRawIron();
                int ironToAdd = rawIron / RAW_IRON_TO_IRON_RATE;

                // Correctly calculate the remaining raw iron
                int newRawIron = rawIron % RAW_IRON_TO_IRON_RATE;

                // Update gameResources with the new amount of iron
                gameResources.addIron(ironToAdd);

                // Update the Base object with the new raw iron amount
                base.setRawIron(newRawIron);
            }
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(BuildingsScreenBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Building building : buildings) {
            Texture textureToDraw = null;
            if (building instanceof Base) {
                textureToDraw = baseIcon;
            } else if (building instanceof AbilityBuilding) {
                textureToDraw = abilityBuildingIcon;
            } else if (building instanceof TankBuilding) {
                textureToDraw = tankBuildingIcon;
            }
            if (textureToDraw != null) {
                game.batch.draw(textureToDraw, building.getPosition().x, building.getPosition().y);
            }
        }
        uiManager.updateResourcesDisplay(game.batch);
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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

    @Override
    public void hide() {
        // This method will be called when this screen is no longer the current screen for a Game.
    }

    @Override
    public void dispose() {
        uiManager.dispose();    }
    private boolean hasAbilityOrTankBuilding() {
        for (Building building : buildings) {
            if (building instanceof AbilityBuilding || building instanceof TankBuilding) {
                return true;
            }
        }
        return false;
    }
    /**
     * Sets up the "Mash" buttons for quick resource generation.
     * This method creates buttons for instantly adding silver and iron to the game resources
     * and adds them to the stage for user interaction.
     */
    private void setupMashButtons() {

        TextButton mashSilver = new TextButton("WIN SILVER", skin);
        TextButton mashIron = new TextButton("WIN IRON", skin);
        mashSilver.setPosition(1500, 150); // Adjust position as needed
        mashIron.setPosition(1500, 250); // Adjust position as needed




        // Repeat for the other buttons, adjusting the number of tanks accordingly
        mashSilver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameResources.setSilver(gameResources.getSilver()+1);}
        });

        mashIron.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
              gameResources.setIron(gameResources.getIron()+2);} }
        );

        // Add buttons to the stage
        stage.addActor(mashSilver);
        stage.addActor(mashIron);
    }
    /**
     * Sets up the tank selection buttons.
     * This method creates buttons for selecting the number of tanks and adds them to the stage.
     * It also includes logic for checking resource availability and updating the game state accordingly.
     */
    private void setupTankSelectionButtons() {

        TextButton twoTanksButton = new TextButton("2 Tanks", skin);
        TextButton threeTanksButton = new TextButton("3 Tanks", skin);
        TextButton fourTanksButton =new TextButton("4 Tanks", skin);
        twoTanksButton.setPosition(900, 150); // Adjust position as needed
        threeTanksButton.setPosition(900, 250); // Adjust position as needed
        fourTanksButton.setPosition(900, 350); // Adjust position as needed



        // Repeat for the other buttons, adjusting the number of tanks accordingly
        twoTanksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameResources.getSilver() < 40  || !hasAbilityOrTankBuilding()) {
                    System.out.println("Not enough silver to buy a tank");
                    return;
                }
                else {
                game.setNumberOfPlayerTanks(2);
                System.out.println("2 Tanks selected");
                gameResources.setSilver(gameResources.getSilver() - 40); // Deduct 20 silver for one tank

            } }
        });

        threeTanksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameResources.getSilver() < 20 || !hasAbilityOrTankBuilding()) {
                    System.out.println("Not enough silver to buy a tank");
                    return;
                }
                else {
                game.setNumberOfPlayerTanks(3);
                System.out.println("3 Tanks selected");
                gameResources.setSilver(gameResources.getSilver() - 60); // Deduct 60 silver for one tank

            } }
        });

        fourTanksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameResources.getSilver() < 20 || !hasAbilityOrTankBuilding()) {
                    System.out.println("Not enough silver to buy a tank");
                    return;
                }else {
                game.setNumberOfPlayerTanks(4);
                System.out.println("4 Tanks selected");
                gameResources.setSilver(gameResources.getSilver() - 80); // Deduct 80 silver for one tank

            } }
        });

        // Add buttons to the stage
        stage.addActor(twoTanksButton);
        stage.addActor(threeTanksButton);
        stage.addActor(fourTanksButton);
    }

    /**
     * Sets up the building buttons.
     * This method creates buttons for constructing different types of buildings and adds them to the stage.
     * It includes listeners for each button to handle the building construction process.
     */
    private void setupBuildingButtons() {
        TextButton abilityBuildingButton = new TextButton("Build Ability Building", skin);
        abilityBuildingButton.setPosition(100, 50); // Adjust position as needed
        abilityBuildingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildAbilityBuilding();
            }
        });

        TextButton tankBuildingButton = new TextButton("Build Tank Building", skin);
        tankBuildingButton.setPosition(300, 50); // Adjust position as needed
        tankBuildingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildTankBuilding();
            }
        });

        TextButton baseBuildingButton = new TextButton("Base Building", skin);
        baseBuildingButton.setPosition(500, 50); // Correct position for baseBuildingButton
        baseBuildingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildBaseBuilding();
            }
        });

        TextButton goToFight = new TextButton("Let the game begin", skin);
        goToFight.setPosition(200, 100); // Correctly set position for goToFight button
        goToFight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("gameResources: " + gameResources);
                game.setScreen(new GameScreen(game, gameResources)); // Change screen to GameScreen
            }
        });

        stage.addActor(abilityBuildingButton);
        stage.addActor(tankBuildingButton);
        stage.addActor(baseBuildingButton);
        stage.addActor(goToFight);
    }

    private void buildAbilityBuilding() {

        if (Building.getIronRequired() > gameResources.getIron()) {
            System.out.println("Not enough iron to build TankBuilding");
        }
       else {        AbilityBuilding abilityBuilding = new AbilityBuilding(new Sprite(abilityBuildingIcon), 1, "AbilityBuilding", 100, 100, 920, 600);

            gameResources.setIron(gameResources.getIron()- AbilityBuilding.getIronRequired());
        addBuilding(abilityBuilding);

    }

    }

    private void buildTankBuilding() {

        if (Building.getIronRequired() > gameResources.getIron()) {
            System.out.println("Not enough iron to build TankBuilding,u are getting fined");
        }
        else {
            TankBuilding tankBuilding = new TankBuilding(new Sprite(tankBuildingIcon), 1, 700, 100,100,100);
            gameResources.setIron(gameResources.getIron()- TankBuilding.getIronRequired());
            addBuilding(tankBuilding);
        }
    }
    private void buildBaseBuilding() {
        int baseCost = 100; // Define the cost of building a new base
        if (gameResources.getIron() >= baseCost) {
            // Position for the new base, adjust as necessary
            float newX = 800;
            float newY = 400;

            // Create the new base
            Base baseBuilding = new Base(new Sprite(baseIcon), 100, newX, newY);

            // Deduct the cost from game resources
            gameResources.setIron(gameResources.getIron() - baseCost);

            // Add the new base to both lists
            bases.add(baseBuilding);
            buildings.add(baseBuilding);

            System.out.println("Base built successfully.");
        } else {
            System.out.println("Not enough iron to build a base.");
        }
    }
    }
