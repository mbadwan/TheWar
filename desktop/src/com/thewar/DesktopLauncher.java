package com.thewar;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.thewar.utils.DatabaseManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		// Connect to the database
		DatabaseManager.connect();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1280, 720);
		config.useVsync(true);
		config.setTitle("theWar");
		theGame gameInstance = new theGame(); // Ensure this is properly initialized

		new Lwjgl3Application(gameInstance, config);
	}
}