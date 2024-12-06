package bricker.paddle;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.ExtraPaddle;
import bricker.gameobjects.Paddle;


/**
 * Factory class to create paddles.
 */
public class PaddleFactory {
    private static final int PADDLE_WIDTH = 150;
    private static final float PADDLE_HEIGHT = 20;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 topLeftCorner = Vector2.ZERO;
    private final Vector2 windowDimensions;

    /**
     * Construct a new PaddleFactory instance.
     *
     * @param imageReader    ImageReader instance.
     * @param inputListener  UserInputListener instance.
     * @param windowDimensions Dimensions of the window.
     */
    public PaddleFactory(ImageReader imageReader, UserInputListener inputListener, Vector2 windowDimensions) {
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Create a new paddle.
     *
     * @param paddleType Type of paddle to create.
     * @return The created paddle.
     */
    public Paddle createPaddle(PaddleType paddleType) {
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);
        if (paddleType == PaddleType.Main) {
            return new Paddle(topLeftCorner, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                    inputListener, windowDimensions);

        } else if (paddleType == PaddleType.Extra) {
            return new ExtraPaddle(topLeftCorner, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                    inputListener, windowDimensions);

        }
        return null;
    }
}
