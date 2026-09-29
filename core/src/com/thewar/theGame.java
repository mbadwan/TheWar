package com.thewar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thewar.screens.LoginScreen;
import com.thewar.screens.MainMenuScreen;
import com.thewar.utils.AssetsManager;
import com.thewar.utils.DataManipulator;
import com.thewar.utils.DatabaseManager;
/**
 * Main class of the game, responsible for initializing and managing the game's lifecycle.
 * This class extends the LibGDX Game class, providing a framework for screen management,
 * asset management, and rendering.
 */
public class theGame extends Game {
	public SpriteBatch batch;
	private AssetsManager assetsManager;
	public static int numberOfPlayerTanks = 1; // Default to 1
	/**
	 * Called when the game is first created. Used to initialize game components.
	 */

	@Override
	public void create () {
		DatabaseManager.connect();
		DatabaseManager.createTable();
		assetsManager = new AssetsManager();
		assetsManager.loadInitialAssets();
		batch = new SpriteBatch();

		this.setScreen(new LoginScreen(this)); // Set initial screen to Loginscreen


		/**
		 *  3 players will be added in the database
		 */
		DataManipulator.addUser("Mo", "Hawai");
		DataManipulator.addUser("Kars", "Ka98");
		DataManipulator.addUser("Aaroni", "Passwort");
	}

	/**
	 * Called each frame, responsible for rendering the game and updating game logic.
	 */
	@Override
	public void render () {
		if (assetsManager.update()) {
			// All assets are loaded, start the game
			super.render();
		} else {

		}
	}
	/**
	 * Called when the game is closing. Used to dispose of game resources.
	 */
	@Override
	public void dispose () {
		assetsManager.dispose();
		DatabaseManager.close();
		batch.dispose();
	}    /**
	 * Gets the game's asset manager.
	 *
	 * @return The asset manager.
	 */
	public AssetsManager getAssetManager() {
		return assetsManager;
	}

	/**
	 * Sets the number of player tanks available in the game.
	 *
	 * @param count The number of tanks.
	 */
	public static void setNumberOfPlayerTanks(int count) {
		numberOfPlayerTanks = count;
	}
	/**
	 * Enum representing the difficulty levels of the game.
	 */
    public enum Difficulty {
		EASY, MEDIUM, HARD
	}   /**
	 * Static class to hold game configuration settings.
	 */
	public static class GameConfig {
		public static Difficulty currentDifficulty = Difficulty.MEDIUM; // Default difficulty
	}
}