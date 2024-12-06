package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents an extra paddle object in the game.
 */
public class ExtraPaddle extends Paddle {
    private Runnable onCounterFinish = null;
    private int collisionCounter = 0;
    private final static int EXTRA_PADDLE_HIT_COUNT = 4;

    /**
     * Construct a new ExtraPaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the
     *                      GameObject will
     *                      not be rendered.
     * @param inputListener The input listener to be used by the object.
     * @param windowDimensions The dimensions of the window in which the object is rendered.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
    }

    /**
     * Called when a collision occurs.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
        if (collisionCounter == EXTRA_PADDLE_HIT_COUNT && this.onCounterFinish != null) {
            this.onCounterFinish.run();
        }
    }

    /**
     * Set the function to be executed when the counter finishes.
     * @param onCounterFinish The function to be executed when the counter finishes.
     */
    public void setOnCounterFinish(Runnable onCounterFinish) {
        this.onCounterFinish = onCounterFinish;
    }
}
