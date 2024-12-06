package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a ball in the game.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter = 0;

    /**
     * Construct a new ball instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject will
     *                      not be rendered.
     * @param collisionSound The sound to be played when the ball collides with something.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * Called when a collision occurs.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;

        Vector2 newVelocity = this.getVelocity().flipped(collision.getNormal());
        this.setVelocity(newVelocity);

        collisionSound.play();
    }

    /**
     * Get the number of collisions that occurred.
     * @return The number of collisions that occurred.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }
}
