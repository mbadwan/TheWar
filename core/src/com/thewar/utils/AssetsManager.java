package com.thewar.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
/**
 * Holds the asset descriptors for the game. This class provides a centralized repository of asset descriptors
 * used throughout the game, ensuring that assets are loaded and accessed in a consistent manner.
 */
public class AssetsManager {
    private final AssetManager assetManager = new AssetManager();

    /**
     * Initiates the loading of initial assets required by the game.
     * This method queues assets for loading by the AssetManager.
     */
    public void loadInitialAssets() {
        assetManager.load(AssetDescriptors.TANK_UP);
        assetManager.load(AssetDescriptors.AITANK);


        assetManager.load(AssetDescriptors.BASE_ICON);
        assetManager.load(AssetDescriptors.ABILITY_BUILDING_ICON);
        assetManager.load(AssetDescriptors.TANK_BUILDING_ICON);
        assetManager.load(AssetDescriptors.PROJECTILES_ICON);
    }
    /**
     * Retrieves an asset that has been loaded by the AssetManager.
     *
     * @param descriptor The AssetDescriptor of the asset to retrieve.
     * @param <T> The type of the asset.
     * @return The asset.
     */
    public <T> T get(AssetDescriptor<T> descriptor) {
        return assetManager.get(descriptor);
    }
    /**
     * Disposes of all assets managed by the AssetManager.
     * This method should be called when the assets are no longer needed, typically at the end of the game.
     */
    public void dispose() {
        assetManager.dispose();
    }
    /**
     * Updates the AssetManager, allowing it to continue loading assets.
     *
     * @return true if all assets have been loaded, false otherwise.
     */
    public boolean update() {
        return assetManager.update(); // Returns true when all assets are loaded
    }

}