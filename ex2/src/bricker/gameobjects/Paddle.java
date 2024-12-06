package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.game.Config;
import bricker.paddle.PaddleType;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle object in the game.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     * @param inputListener The input listener that will be used to control the bricker.paddle.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {

        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Update the object's state. Move the paddle according to the input received.
     *
     * @param deltaTime Time elapsed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDirection = Vector2.ZERO;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDirection = movementDirection.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }


        if (this.getTopLeftCorner().x() <= 2 * Config.WALL_WIDTH) {
            if (movementDirection.x() < 0) {
                movementDirection = Vector2.ZERO;
            }
        } else if (this.getTopLeftCorner().x() + this.getDimensions().x() >= windowDimensions.x() - 2 * Config.WALL_WIDTH) {
            if (movementDirection.x() > 0) {
                movementDirection = Vector2.ZERO;
            }
        }

        this.setVelocity(movementDirection.mult(MOVEMENT_SPEED));
    }
}