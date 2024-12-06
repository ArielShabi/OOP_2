package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.game.AddGameObjectFunction;
import bricker.game.RemoveGameObjectFunction;
import bricker.gameobjects.ExtraPaddle;
import bricker.paddle.PaddleFactory;
import bricker.paddle.PaddleType;

import java.util.function.Consumer;

/**
 * A strategy that creates an extra paddle when a collision occurs.
 */
public class ExtraPaddleCollisionStrategy extends BasicCollisionStrategy {
    private final AddGameObjectFunction addPaddleFunction;
    private final RemoveGameObjectFunction removePaddleFunction;
    private final PaddleFactory paddleFactory;
    private final Vector2 windowDimensions;
    private ExtraPaddle extraPaddle = null;
    private final Counter extraPaddleCounter;

    /**
     * Constructor for the extra paddle collision strategy.
     * @param removeGameObjectFunction The function to remove the game object.
     * @param addPaddleFunction The function to add a paddle object.
     * @param removePaddleFunction The function to remove a paddle object.
     * @param paddleFactory The factory for creating paddles.
     * @param windowDimensions The dimensions of the game window.
     * @param extraPaddleCounter The counter for the extra paddles.
     */
    public ExtraPaddleCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                        AddGameObjectFunction addPaddleFunction,
                                        RemoveGameObjectFunction removePaddleFunction, PaddleFactory paddleFactory,
                                        Vector2 windowDimensions, Counter extraPaddleCounter) {
        super(removeGameObjectFunction);
        this.addPaddleFunction = addPaddleFunction;
        this.removePaddleFunction = removePaddleFunction;
        this.paddleFactory = paddleFactory;
        this.windowDimensions = windowDimensions;
        this.extraPaddleCounter = extraPaddleCounter;
    }

    /**
     * Creates an extra paddle when a collision occurs.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);
        if (extraPaddleCounter.value() != 0) {
            return;
        }
        this.extraPaddle = (ExtraPaddle) paddleFactory.createPaddle(PaddleType.Extra);
        extraPaddleCounter.increment();
        extraPaddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));
        this.addPaddleFunction.run(extraPaddle, Layer.DEFAULT);
        extraPaddle.setOnCounterFinish(this::onCounterFinish);
    }

    private void onCounterFinish() {
        this.removePaddleFunction.run(extraPaddle, Layer.DEFAULT);
        extraPaddleCounter.decrement();
    }

}
