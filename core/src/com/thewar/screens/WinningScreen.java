// WinningScreen.java
package com.thewar.screens;

import static com.badlogic.gdx.Gdx.gl;
import static com.thewar.utils.SessionManager.getCurrentUserId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.thewar.logic.GameResources;
import com.thewar.theGame;
/**
 * Represents the screen displayed to the player upon winning the game.
 * This screen shows a congratulatory message along with the final resource counts.
 */
public class WinningScreen implements Screen {

    private Stage stage;
    private theGame game;
    private Texture backgroundTexture;
    private GameResources gameResources;
    /**
     * Constructs a WinningScreen with the game instance and the final game resources.
     *
     * @param game The game instance this screen is part of.
     * @param resources The final game resources to display.
     */
    public WinningScreen(theGame game, GameResources resources) {

        this.gameResources = resources;

        this.game = game;
    }
    /**
     * Initializes the screen, setting up UI components and event listeners.
     */
    @Override
    public void show() {
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Label winningLabel = new Label("Congratulations! You've won!", skin);
        backgroundTexture = new Texture("WinningScreen.png");
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);

        float centerX = stage.getWidth() / 2 - winningLabel.getWidth() / 2;
        float centerY = stage.getHeight() / 2 - winningLabel.getHeight() / 2;
        winningLabel.setPosition(centerX, centerY);
        stage.addActor(winningLabel);

        // Retrieve the amounts
        int silverAmount = gameResources.getSilver();
        int ironAmount = gameResources.getIron();

        // Create labels for silver and iron amounts
        Label silverLabel = new Label("Silver: " + silverAmount, skin);
        Label ironLabel = new Label("Iron: " + ironAmount, skin);

        // Position the silver and iron labels below the winning message
        silverLabel.setPosition(centerX, centerY - 50); // Adjust Y position as needed
        ironLabel.setPosition(centerX, centerY - 100); // Adjust Y position as needed

        // Add the labels to the stage
        stage.addActor(silverLabel);
        stage.addActor(ironLabel);

        // Update resources in the database
        int userId = getCurrentUserId();
        com.thewar.utils.DataManipulator.updateResources(userId, silverAmount, ironAmount);
    }
    /**
     * Renders the winning screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    /**
     * Disposes of the resources used by the WinningScreen.
     */
    @Override
    public void dispose() {  backgroundTexture.dispose();
        stage.dispose();}
}