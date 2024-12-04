package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import paddle.PaddleType;

public class ExtraPaddle extends Paddle {

    private Runnable onCounterFinish = null;
    private int collisionCounter = 0;
    private final static int EXTRA_PADDLE_HIT_COUNT = 4;

    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable, inputListener);
        this.paddleType = PaddleType.Extra;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCounter++;
        if (collisionCounter == EXTRA_PADDLE_HIT_COUNT && this.onCounterFinish != null) {
            this.onCounterFinish.run();
        }
    }

    public void setOnCounterFinish(Runnable onCounterFinish) {
        this.onCounterFinish = onCounterFinish;
    }
}
