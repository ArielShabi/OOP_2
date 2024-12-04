package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import game.RemoveGameObjectFunction;

import java.util.function.Consumer;

public class BasicCollisionStrategy implements CollisionStrategy {

    public final Consumer<GameObject> removeGameObjectFunction;

    public BasicCollisionStrategy(Consumer<GameObject> removeGameObjectFunction) {
        this.removeGameObjectFunction = removeGameObjectFunction;
    }

    @Override
    public void onCollision(GameObject a, GameObject b) {
        removeGameObjectFunction.accept(a);
    }
}
