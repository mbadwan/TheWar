package com.thewar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.theGame;
import com.thewar.utils.AssetsManager;
/**
 * Represents the main menu screen of the game, providing options to play the game,
 * choose difficulty, or exit the game.
 */
public class MainMenuScreen implements Screen {
    private static final int DIFFICULTY_BUTTON_WIDTH = 286;
    private static final int DIFFICULTY_BUTTON_HEIGHT = 48;
    private static final int EASY_BUTTON_Y = 300;
    private static final int MEDIUM_BUTTON_Y = 350;
    private static final int HARD_BUTTON_Y = 400;
    private static final int PLAY_BUTTON_WIDTH = 286;
    private static final int PLAY_BUTTON_HEIGHT = 48;
    private static final int PLAY_BUTTON_Y = 100;

    private static final int EXIT_BUTTON_WIDTH = 286;
    private static final int EXIT_BUTTON_HEIGHT = 48;
    private static final int EXIT_BUTTON_Y = 15;


    public Viewport viewport;
    public OrthographicCamera camera;

    theGame game;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;

    Texture background;
    Texture easyButtonActive, easyButtonInactive;
    Texture mediumButtonActive, mediumButtonInactive;
    Texture hardButtonActive, hardButtonInactive;
    /**
     * Constructs the MainMenuScreen with a reference to the game instance.
     * Initializes the camera and viewport for UI rendering.
     *
     * @param game The game instance this screen is part of.
     */
    public MainMenuScreen(theGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera); // Changed to ScreenViewport

        /**
         * Loads and initializes textures for UI elements.
         */
        playButtonActive = new Texture("PlayActive.png");
        playButtonInactive = new Texture("Play.png");
        exitButtonActive = new Texture("ExitActive.png");
        exitButtonInactive = new Texture("Exit.png");

        easyButtonActive = new Texture("easyActive.png");
        easyButtonInactive = new Texture("easyInactive.png");
        mediumButtonActive = new Texture("mediumActive.png");
        mediumButtonInactive = new Texture("mediumInactive.png");
        hardButtonActive = new Texture("YouAreChallengedActive.png");
        hardButtonInactive = new Texture("YourAreChallenged.png");
        background = new Texture("background.jpeg");
    }

    @Override
    public void show() {
    }
    /**
     * Renders the main menu screen, including the background and buttons.
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // background image
        game.batch.draw(background, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());

        // Easy button
        int xEasy = (viewport.getScreenWidth() / 2) - (DIFFICULTY_BUTTON_WIDTH / 2); // Center horizontally
        if (Gdx.input.getX() < xEasy + DIFFICULTY_BUTTON_WIDTH && Gdx.input.getX() > xEasy &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < EASY_BUTTON_Y + DIFFICULTY_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > EASY_BUTTON_Y) {
            game.batch.draw(easyButtonActive, xEasy, EASY_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                theGame.GameConfig.currentDifficulty = theGame.Difficulty.EASY;
            }
        } else {
            game.batch.draw(easyButtonInactive, xEasy, EASY_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
        }
        // Medium button

        int xMedium = (viewport.getScreenWidth() / 2) - (DIFFICULTY_BUTTON_WIDTH / 2); // Center horizontally for the medium button
        if (Gdx.input.getX() < xMedium + DIFFICULTY_BUTTON_WIDTH && Gdx.input.getX() > xMedium &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < MEDIUM_BUTTON_Y + DIFFICULTY_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > MEDIUM_BUTTON_Y) {
            game.batch.draw(mediumButtonActive, xMedium, MEDIUM_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                theGame.GameConfig.currentDifficulty = theGame.Difficulty.MEDIUM;
            }
        } else {
            game.batch.draw(mediumButtonInactive, xMedium, MEDIUM_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
        }
        // Hard button
        int xHard = (viewport.getScreenWidth() / 2) - (DIFFICULTY_BUTTON_WIDTH / 2); // Center horizontally for the hard button
        if (Gdx.input.getX() < xHard + DIFFICULTY_BUTTON_WIDTH && Gdx.input.getX() > xHard &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < HARD_BUTTON_Y + DIFFICULTY_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > HARD_BUTTON_Y) {
            game.batch.draw(hardButtonActive, xHard, HARD_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                theGame.GameConfig.currentDifficulty = theGame.Difficulty.HARD;
            }
        } else {
            game.batch.draw(hardButtonInactive, xHard, HARD_BUTTON_Y, DIFFICULTY_BUTTON_WIDTH, DIFFICULTY_BUTTON_HEIGHT);
        }


        // Handle Play Button
        int x = (viewport.getScreenWidth() / 2) - (PLAY_BUTTON_WIDTH / 2); // Adjusted for ScreenViewport

        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > PLAY_BUTTON_Y) {

            game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);

            if (Gdx.input.justTouched()) {
                System.out.println("Play button clicked");
                this.dispose();
                System.out.println("Difficulty: " + theGame.GameConfig.currentDifficulty);
                // Inside MainMenuScreen, when transitioning to GameScreen
                if (game != null) {
                    game.setScreen(new BuildingsScreen(game));
                } else {
                    System.out.println("Error: 'game' is null when attempting to transition to GameScreen.");
                }
            }

        } else {
            game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        // Handle Exit Button
        x = (viewport.getScreenWidth() / 2) - (EXIT_BUTTON_WIDTH / 2); // Adjusted for ScreenViewport

        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > EXIT_BUTTON_Y) {

            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

            if (Gdx.input.justTouched()) {
                System.out.println("Exit button clicked");
                Gdx.app.exit();
            }

        } else {
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        game.batch.end();
    }


    @Override
    public void resize(int width, int height) {        viewport.update(width, height, true);

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        playButtonActive.dispose();
        playButtonInactive.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        background.dispose();    }
}
