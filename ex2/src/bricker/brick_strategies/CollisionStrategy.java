package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for collision strategies.
 */
public interface CollisionStrategy {
    /**
     * Method to be called when a collision occurs.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    void onCollision(GameObject a, GameObject b);
}
