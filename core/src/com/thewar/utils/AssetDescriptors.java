package com.thewar.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
/**
 * Holds the asset descriptors for the game. This class provides a centralized repository of asset descriptors
 * used throughout the game, ensuring that assets are loaded and accessed in a consistent manner.
 */
public class AssetDescriptors {
    public static final AssetDescriptor<Texture> TANK_UP = new AssetDescriptor<Texture>("tank_up.jpeg", Texture.class);
    public static final AssetDescriptor<Texture> AITANK = new AssetDescriptor<Texture>("AITank.png", Texture.class);
    public static final AssetDescriptor<Texture> BASE_ICON = new AssetDescriptor<Texture>("baseIcon.png", Texture.class);
    public static final AssetDescriptor<Texture> ABILITY_BUILDING_ICON = new AssetDescriptor<Texture>("abilityBuildingIcon.png", Texture.class);
    public static final AssetDescriptor<Texture> TANK_BUILDING_ICON = new AssetDescriptor<Texture>("tankBuildingIcon.png", Texture.class);
    public static final AssetDescriptor<Texture> PROJECTILES_ICON = new AssetDescriptor<Texture>("Projectile.png", Texture.class);
}
