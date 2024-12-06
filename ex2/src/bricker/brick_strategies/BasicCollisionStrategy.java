package bricker.brick_strategies;

import danogl.GameObject;
import java.util.function.Consumer;

/**
 * Basic collision strategy that removes the first object in the collision.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    private final Consumer<GameObject> removeGameObjectFunction;

    /**
     * Constructor for the basic collision strategy.
     * @param removeGameObjectFunction The function to remove the game object.
     */
    public BasicCollisionStrategy(Consumer<GameObject> removeGameObjectFunction) {
        this.removeGameObjectFunction = removeGameObjectFunction;
    }

    /**
     * Removes the first object in the collision.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    @Override
    public void onCollision(GameObject a, GameObject b) {
        removeGameObjectFunction.accept(a);
    }
}
