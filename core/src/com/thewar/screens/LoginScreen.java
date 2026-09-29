package com.thewar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewar.theGame;
import com.thewar.utils.DataManipulator;
import com.thewar.utils.SessionManager;
/**
 * Constructs a new {@code LoginScreen} with a reference to the game instance.
 * Initializes textures and sets up the camera and viewport for rendering.
 */
public class LoginScreen implements Screen {

    private static final int LOGIN_BUTTON_WIDTH = 94;
    private static final int LOGIN_BUTTON_HEIGHT = 48;
    private static final int LOGIN_BUTTON_Y = 200;
    private Stage stage;
    private TextField usernameTextField, passwordTextField;
    private Skin skin; // Assuming you have a Skin for your TextFields
    private static final int REGISTER_BUTTON_WIDTH = 286;
    private static final int REGISTER_BUTTON_HEIGHT = 40;
    private static final int REGISTER_BUTTON_Y = 150;
    private static final int CANCEL_BUTTON_HEIGHT = 40;
    private static final int CANCEL_BUTTON_Y = 100;
    private static final int CANCEL_BUTTON_WIDTH = 286;

    public OrthographicCamera camera;
    private Viewport viewport;
    theGame game;
    Texture loginButtonActive;
    Texture loginButtonInactive;
    Texture cancelButtonActive;
    Texture cancelButtonInactive;
    Texture registerButtonActive;
    Texture registerButtonInactive;
    Texture loginBackground;
    public LoginScreen(theGame game) {
        this.game = game;
        loginButtonActive = new Texture("LoginActive.png");
        loginButtonInactive = new Texture("Login.png");
        cancelButtonActive = new Texture("ExitActive.png");
        cancelButtonInactive = new Texture("Exit.png");
        registerButtonActive = new Texture("RegisterAcitve.png");
        registerButtonInactive = new Texture("Register.png");
        loginBackground = new Texture("loginBackground.png");
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

    }

    /**
     * Called when this screen becomes the current screen.
     * Initializes the stage, input processor, and UI elements.
     */
    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage); // Set input processor to stage
        int xPosition = (viewport.getScreenWidth() / 2) - (LOGIN_BUTTON_WIDTH / 2); // Calculate x position based on login button

        // Initialize and configure the username text field
        usernameTextField = new TextField("", skin);
        usernameTextField.setMessageText("Username");
        usernameTextField.setPosition(xPosition-100, LOGIN_BUTTON_Y + LOGIN_BUTTON_HEIGHT + 60);
        usernameTextField.setSize(286, 40);

        // Initialize and configure the password text field
        passwordTextField = new TextField("", skin);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPosition(xPosition-100, LOGIN_BUTTON_Y + LOGIN_BUTTON_HEIGHT + 10);
        passwordTextField.setSize(286, 40);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        // Add the text fields to the stage
        stage.addActor(usernameTextField);
        stage.addActor(passwordTextField);
    }
    /**
     * Renders the login screen, including the background, buttons, and text fields.handels the buttons
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // Background image
        game.batch.draw(loginBackground, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
// Handle Register Button
        int xRegister = (viewport.getScreenWidth() / 2) - (REGISTER_BUTTON_WIDTH / 2);

        if (Gdx.input.getX() < xRegister + REGISTER_BUTTON_WIDTH && Gdx.input.getX() > xRegister &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < REGISTER_BUTTON_Y + REGISTER_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > REGISTER_BUTTON_Y) {

            game.batch.draw(registerButtonActive, xRegister, REGISTER_BUTTON_Y, REGISTER_BUTTON_WIDTH, REGISTER_BUTTON_HEIGHT);

            if (Gdx.input.justTouched()) {
                // Placeholder for register action
                System.out.println("Register button clicked");
                handleRegistrationButtonClick();
            }

        } else {
            game.batch.draw(registerButtonInactive, xRegister, REGISTER_BUTTON_Y, REGISTER_BUTTON_WIDTH, REGISTER_BUTTON_HEIGHT);
        }

        // Handle Login Button
        int x = (viewport.getScreenWidth() / 2) - (LOGIN_BUTTON_WIDTH / 2);

        if (Gdx.input.getX() < x + LOGIN_BUTTON_WIDTH && Gdx.input.getX() > x &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < LOGIN_BUTTON_Y + LOGIN_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > LOGIN_BUTTON_Y) {

            game.batch.draw(loginButtonActive, x, LOGIN_BUTTON_Y, LOGIN_BUTTON_WIDTH, LOGIN_BUTTON_HEIGHT);

            if (Gdx.input.justTouched()) {
                String username = getUsernameInput();
                String password = getPasswordInput();
                int userId = DataManipulator.getUserId(username, password);
                if (userId != -1) {
                    System.out.println("Login successful");
                    SessionManager.setCurrentUserId(userId); // Store the user ID
                    if (game != null) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                } else {
                    System.out.println("Login failed. Incorrect username or password.");
                    usernameTextField.setText("");
                    passwordTextField.setText("");
                }
            }

        } else {
            game.batch.draw(loginButtonInactive, x, LOGIN_BUTTON_Y, LOGIN_BUTTON_WIDTH, LOGIN_BUTTON_HEIGHT);
        }


        // Handle Cancel Button
        x = (viewport.getScreenWidth() / 2) - (CANCEL_BUTTON_WIDTH / 2);
        if (Gdx.input.getX() < x + CANCEL_BUTTON_WIDTH && Gdx.input.getX() > x &&
                Gdx.graphics.getHeight() - Gdx.input.getY() < CANCEL_BUTTON_Y + CANCEL_BUTTON_HEIGHT &&
                Gdx.graphics.getHeight() - Gdx.input.getY() > CANCEL_BUTTON_Y) {
            game.batch.draw(cancelButtonActive, x, CANCEL_BUTTON_Y, CANCEL_BUTTON_WIDTH, CANCEL_BUTTON_HEIGHT);
            if (Gdx.input.justTouched()) {
                // Close the application
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(cancelButtonInactive, x, CANCEL_BUTTON_Y, CANCEL_BUTTON_WIDTH, CANCEL_BUTTON_HEIGHT);
        }
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {    viewport.update(width, height, true);

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
    /**
     * Handles the click event on the registration button.
     * This method performs input validation, checks if the user already exists,
     * and attempts to register a new user with the provided username and password.
     * Upon successful registration, it clears the input fields. If the registration fails
     * or if the user already exists, it provides appropriate feedback to the user.
     */    public void handleRegistrationButtonClick() {
        String username = getUsernameInput(); // Retrieve username from UI
        String password = getPasswordInput(); // Retrieve password from UI

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty");
            return;
        }

        // Check if user exists in the database
        if (DataManipulator.userExists(username)) {
            System.out.println("User already exists.");
            return;
        }

        boolean registrationSuccess = DataManipulator.addUser(username, password);
        if (registrationSuccess) {
            System.out.println("Registration successful");
            usernameTextField.setText("");
            passwordTextField.setText("");        } else {
            System.out.println("Registration failed.");
        }
    }

    private String getPasswordInput() {       return passwordTextField.getText();
    }

    private String getUsernameInput() {
        return usernameTextField.getText();
    }


}