package bricker.brick_strategies;

import bricker.collected_strategy.CollectedStrategy;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.game.AddGameObjectFunction;
import bricker.game.RemoveGameObjectFunction;
import bricker.gameobjects.Collectable;

import java.util.function.Consumer;

/**
 * A strategy that creates a collectable object when a collision occurs.
 */
public class CollectableCollisionStrategy extends BasicCollisionStrategy {
    private static final float MOVEMENT_SPEED = 100;
    private static final float COLLECTABLE_SIZE = 20;

    private final AddGameObjectFunction addGameObjectFunction;
    private final Renderable renderable;
    private final CollectedStrategy collectedStrategy;
    private final GameObject collectorObject;
    private final RemoveGameObjectFunction removeCollectableFunction;

    /**
     * Constructor for the collectable collision strategy.
     * @param removeGameObjectFunction The function to remove the game object.
     * @param addGameObjectFunction The function to add a game object.
     * @param removeCollectableFunction The function to remove a collectable object.
     * @param renderable The renderable representing the object.
     * @param collectedStrategy The strategy to be executed when the object is collected.
     * @param collectorObject The object that collects the collectable.
     */
    public CollectableCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                        AddGameObjectFunction addGameObjectFunction,
                                        RemoveGameObjectFunction removeCollectableFunction,
                                        Renderable renderable, CollectedStrategy collectedStrategy,
                                        GameObject collectorObject) {
        super(removeGameObjectFunction);
        this.addGameObjectFunction = addGameObjectFunction;
        this.renderable = renderable;
        this.collectedStrategy = collectedStrategy;
        this.collectorObject = collectorObject;
        this.removeCollectableFunction = removeCollectableFunction;
    }

    /**
     * Creates a collectable object when a collision occurs.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);

        Collectable collectable = new Collectable(Vector2.ZERO, new Vector2(COLLECTABLE_SIZE, COLLECTABLE_SIZE),
                renderable, collectedStrategy, removeCollectableFunction, collectorObject);

        collectable.setCenter(a.getCenter());
        collectable.setVelocity(Vector2.DOWN.mult(MOVEMENT_SPEED));

        this.addGameObjectFunction.run(collectable, Layer.DEFAULT);
    }
}
