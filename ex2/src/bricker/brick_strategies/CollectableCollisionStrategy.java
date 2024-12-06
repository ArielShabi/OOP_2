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

public class CollectableCollisionStrategy extends BasicCollisionStrategy {
    private static final float MOVEMENT_SPEED = 100;
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

        collectable.setCenter(a.getCenter());
        collectable.setVelocity(Vector2.DOWN.mult(MOVEMENT_SPEED));

        this.addGameObjectFunction.run(collectable, Layer.DEFAULT);
    }
}
