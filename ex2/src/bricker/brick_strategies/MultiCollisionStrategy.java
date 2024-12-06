package bricker.brick_strategies;

import danogl.GameObject;

import java.util.function.Consumer;

/**
 * MultiCollisionStrategy class that extends BasicCollisionStrategy.
 */
public class MultiCollisionStrategy extends BasicCollisionStrategy {
    private final CollisionStrategy collisionStrategyA;
    private final CollisionStrategy collisionStrategyB;

    /**
     * Constructor for the MultiCollisionStrategy.
     * @param removeGameObjectFunction The function to remove the game object.
     * @param collisionStrategyA The first collision strategy.
     * @param collisionStrategyB The second collision strategy.
     */
    public MultiCollisionStrategy(Consumer<GameObject> removeGameObjectFunction, CollisionStrategy collisionStrategyA, CollisionStrategy collisionStrategyB) {
        super(removeGameObjectFunction);
        this.collisionStrategyA = collisionStrategyA;
        this.collisionStrategyB = collisionStrategyB;
    }

    /**
     * Calls the onCollision method of the two collision strategies.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);
        collisionStrategyA.onCollision(a, b);
        collisionStrategyB.onCollision(a, b);
    }
}
