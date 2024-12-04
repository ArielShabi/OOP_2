package brick_strategies;

import collected_strategy.CollectedStrategy;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Collectable;

import java.util.function.Consumer;

public class CollectableCollisionStrategy extends BasicCollisionStrategy {
    private static final float MOVEMENT_SPEED = 200;
    private static final float COLLECTABLE_SIZE = 20;

    private final AddGameObjectFunction addGameObjectFunction;
    private final Renderable renderable;
    private final CollectedStrategy collectedStrategy;
    private final GameObject collectorObject;
    private final RemoveGameObjectFunction removeCollectableFunction;

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

    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);

        Collectable collectable = new Collectable(Vector2.ZERO, new Vector2(COLLECTABLE_SIZE, COLLECTABLE_SIZE),
                renderable, collectedStrategy, removeCollectableFunction, collectorObject);

        Vector2 heartPosition = new Vector2(a.getCenter().x(), a.getCenter().y() + COLLECTABLE_SIZE); //to check
        // without +HEART_SIZE
        collectable.setCenter(heartPosition);
        collectable.setVelocity(Vector2.DOWN.mult(MOVEMENT_SPEED));

        this.addGameObjectFunction.run(collectable, Layer.DEFAULT);
    }
}
