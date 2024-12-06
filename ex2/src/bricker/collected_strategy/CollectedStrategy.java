package bricker.collected_strategy;

import danogl.GameObject;
import bricker.gameobjects.Collectable;

/**
 * Interface for collected strategies.
 */
public interface CollectedStrategy {

    /**
     * Method to be called when a collectable is collected.
     * @param mainObj The object that collected the collectable.
     * @param toCollect The collectable that was collected.
     */
    void collectOnCollision(GameObject mainObj, Collectable toCollect);
}
