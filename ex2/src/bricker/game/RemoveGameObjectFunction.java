package bricker.game;

import danogl.GameObject;

/**
 * Interface for the remove game object function.
 */
public interface RemoveGameObjectFunction {
    /**
     * Method to be called when a game object is removed.
     * @param gameObject The game object to be removed.
     * @param layer The layer to remove the game object from.
     * @return True if the game object was removed, false otherwise.
     */
    boolean run(GameObject gameObject, int layer);
}
