package brick_strategies;

import danogl.GameObject;
import game.RemoveGameObjectFunction;

public class BasicCollisionStrategy implements CollisionStrategy {

    private final RemoveGameObjectFunction removeGameObjectFunction;

    public BasicCollisionStrategy(RemoveGameObjectFunction removeGameObjectFunction) {
        this.removeGameObjectFunction = removeGameObjectFunction;
    }

    @Override
    public void onCollision(GameObject a, GameObject b) {
        removeGameObjectFunction.run(a);
    }
}
