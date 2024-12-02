package brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject a, GameObject b);
}
