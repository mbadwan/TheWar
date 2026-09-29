package com.thewar.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * Manages the UI elements related to displaying game resources.
 * This class is responsible for rendering resource information on the screen.
 */

public class UIManager {
    private BitmapFont font;
    private GameResources gameResources;
    private SpriteBatch batch;

    /**
     * Constructs a UIManager with specified game resources and sprite batch.
     *
     * @param gameResources The game resources to be displayed.
     * @param batch The sprite batch used for drawing.
     */
    public UIManager(GameResources gameResources, SpriteBatch batch) {
        this.gameResources = gameResources;
        this.batch = batch;

        this.font = new BitmapFont();
    }
    /**
     * Updates and displays the resources information on the screen.
     * This method draws the current amounts of iron, silver, raw iron, and raw silver.
     *
     * @param batch The sprite batch used for drawing the text.
     */
    public void updateResourcesDisplay(SpriteBatch batch) {
        String resourcesText = "Iron: " + gameResources.getIron() +
                ", Silver: " + gameResources.getSilver() +
                ", Raw Iron: " + gameResources.getRawIron() +
                ", Raw Silver: " + gameResources.getRawSilver();
        font.draw(this.batch, resourcesText, 10, Gdx.graphics.getHeight() - 10); // Adjust position based on your UI layout
    }

    public void dispose() {
        font.dispose();
    }
}