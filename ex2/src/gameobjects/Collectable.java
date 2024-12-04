package gameobjects;

import brick_strategies.CollisionStrategy;
import collected_strategy.CollectedStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Collectable extends GameObject {
    public final CollectedStrategy collectedStrategy;
    public final GameObject originalPaddle;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                          top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case the
     *                          GameObject will
     *                          not be rendered.
     * @param collectedStrategy The strategy to be executed when the object is collected.
     */
    public Collectable(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       CollectedStrategy collectedStrategy, GameObject originalPaddle) {
        super(topLeftCorner, dimensions, renderable);
        this.collectedStrategy = collectedStrategy;
        this.originalPaddle = originalPaddle;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collectedStrategy.collectOnCollision(other, this);
    }

    // collide only with original paddle
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == originalPaddle;
    }


}


