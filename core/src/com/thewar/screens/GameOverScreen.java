package com.thewar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thewar.logic.GameResources;
import com.thewar.logic.UIManager;
import com.thewar.theGame;
import com.thewar.utils.AssetsManager;
/**
 * Represents the game over screen displayed when the game ends.
 * This screen provides the player with the option to return to the main menu.
 */
public class GameOverScreen implements Screen {
    private theGame game;
    private Stage stage;
    private UIManager uiManager;
    Texture gameoverbackground ;
    private GameResources gameResources;
    /**
     * Constructs a new GameOverScreen instance.
     *
     * @param game The game instance this screen is part of.
     * @param gameResources The game resources to be managed and displayed.
     */
    public GameOverScreen(theGame game, GameResources gameResources) {
        this.game = game;
        this.gameResources = gameResources;
        this.uiManager = new UIManager(this.gameResources, game.batch);

    }
    /**
     * Initializes the screen, setting up UI components and event listeners.
     */
    @Override
    public void show() {

      gameoverbackground  = new Texture("gameroverbackground.png");
        stage = new Stage(new ScreenViewport(), game.batch);
        Gdx.input.setInputProcessor(stage);


        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton mainMenuButton = new TextButton("Main Menu", uiSkin);
        mainMenuButton.setPosition(Gdx.graphics.getWidth() / 2f - mainMenuButton.getWidth() / 2, Gdx.graphics.getHeight() / 4f);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change to the main menu screen
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(mainMenuButton);
    }
    /**
     * Renders the game over screen.
     *
     * @param delta The time in seconds since the last render.
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(gameoverbackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();
    }

        @Override
    public void resize(int width, int height) {
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
    }
}