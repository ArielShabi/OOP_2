package brick_strategies;

import danogl.GameObject;

import java.util.function.Consumer;

public class MultiCollisionStrategy extends BasicCollisionStrategy {
    private final CollisionStrategy collisionStrategyA;
    private final CollisionStrategy collisionStrategyB;

    public MultiCollisionStrategy(Consumer<GameObject> removeGameObjectFunction, CollisionStrategy collisionStrategyA, CollisionStrategy collisionStrategyB) {
        super(removeGameObjectFunction);
        this.collisionStrategyA = collisionStrategyA;
        this.collisionStrategyB = collisionStrategyB;
    }

    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);
        collisionStrategyA.onCollision(a, b);
        collisionStrategyB.onCollision(a, b);
    }
}
