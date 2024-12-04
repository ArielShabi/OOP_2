package paddle;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.ExtraPaddle;
import gameobjects.Paddle;

public class PaddleFactory {
    private static final int PADDLE_WIDTH = 150;
    private static final float PADDLE_HEIGHT = 20;
    private final ImageReader imageReader;
    private final UserInputListener inputListener;
    private final Vector2 topLeftCorner = Vector2.ZERO;

    public PaddleFactory(ImageReader imageReader, UserInputListener inputListener) {
        this.imageReader = imageReader;
        this.inputListener = inputListener;
    }

    public Paddle createPaddle(PaddleType paddleType) {
        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);
        if (paddleType == PaddleType.Main) {
            return new Paddle(topLeftCorner, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                    inputListener);

        } else if (paddleType == PaddleType.Extra) {
            return new ExtraPaddle(topLeftCorner,new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),paddleImage,
                    inputListener);

        }
        return null;
    }
}
