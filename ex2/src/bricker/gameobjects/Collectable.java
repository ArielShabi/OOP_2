package bricker.gameobjects;

import bricker.collected_strategy.CollectedStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.game.RemoveGameObjectFunction;

/**
 * Represents a collectable object in the game.
 */
public class Collectable extends GameObject {
    public final CollectedStrategy collectedStrategy;
    private final RemoveGameObjectFunction removeCollectableFunction;
    public final GameObject originalPaddle;

    /**
     * Construct a new Collectable instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                          top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case the
     *                          GameObject will
     *                          not be rendered.
     * @param collectedStrategy The strategy to be executed when the object is collected.
     * @param removeCollectableFunction The function to be executed when the object is removed.
     * @param originalPaddle The original paddle object, used to check if the object should collide with it.
     */
    public Collectable(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       CollectedStrategy collectedStrategy, RemoveGameObjectFunction removeCollectableFunction, GameObject originalPaddle) {
        super(topLeftCorner, dimensions, renderable);
        this.collectedStrategy = collectedStrategy;
        this.removeCollectableFunction = removeCollectableFunction;
        this.originalPaddle = originalPaddle;
    }

    /**
     * Called when a collision occurs. The object is collected and the collectable is removed from the game.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collectedStrategy.collectOnCollision(other, this);
        removeCollectableFunction.run(this, Layer.DEFAULT);
    }

    /**
     * Check if the object should collide with another object.
     * @param other The GameObject with which a collision occurred.
     * @return True if the object is the original paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == originalPaddle;
    }


}


