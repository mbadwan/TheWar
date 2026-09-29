package com.thewar.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input.Buttons;
/**
 * Handles mouse input for interactive screens within the game.
 * This class extends InputAdapter to process mouse clicks and movements,
 * facilitating interaction with the game world through mouse input.
 */
public class MouseInputProcessor extends InputAdapter {
    private final InteractiveScreen interactiveScreen;

    /**
     * Constructs a MouseInputProcessor for a specific interactive screen.
     *
     * @param interactiveScreen The interactive screen that will process the mouse input.
     */
    public MouseInputProcessor(InteractiveScreen interactiveScreen) {
        this.interactiveScreen = interactiveScreen;
    }
    /**
     * Processes a touch down event (mouse click or screen touch).
     * This method is called when a mouse button is pressed or the screen is touched.
     * It handles left-click to set a destination and right-click to create a projectile.
     *
     * @param screenX The x-coordinate of the touch, in screen coordinates.
     * @param screenY The y-coordinate of the touch, in screen coordinates.
     * @param pointer The pointer for the event.
     * @param button The button that was pressed.
     * @return true to indicate the event was handled.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Viewport viewport = interactiveScreen.getViewport();
        if (button == Buttons.LEFT) {
            float x = viewport.unproject(new Vector2(screenX, screenY)).x;
            float y = viewport.unproject(new Vector2(screenX, screenY)).y;
            interactiveScreen.setDestination(x, y);
        } else if (button == Input.Buttons.RIGHT) {
            interactiveScreen.createProjectileAt(screenX, screenY);
            return true;
        }
        return true;
    }
}