package bricker.game;

import danogl.GameObject;

/**
 * Interface for the add game object function.
 */
public interface AddGameObjectFunction {
    /**
     * Method to be called when a game object is added.
     * @param gameObject The game object to be added.
     * @param layer The layer to add the game object to.
     */
    void run(GameObject gameObject, int layer);
}
