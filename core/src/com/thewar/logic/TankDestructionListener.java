package com.thewar.logic;
/**
 * Defines the behavior for objects that listen to tank destruction events.
 * Implementations of this interface will be notified when a tank is destroyed.
 */
public interface TankDestructionListener {
    /**
     * Called when a tank is destroyed.
     *
     * @param tank The tank that was destroyed.
     */
    void onTankDestroyed(Tank tank);
}