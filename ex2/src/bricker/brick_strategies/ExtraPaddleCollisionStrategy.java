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


public class ExtraPaddleCollisionStrategy extends BasicCollisionStrategy {
    private final AddGameObjectFunction addPaddleFunction;
    private final RemoveGameObjectFunction removePaddleFunction;
    private final PaddleFactory paddleFactory;
    private final Vector2 windowDimensions;
    private ExtraPaddle extraPaddle = null;
    private final Counter extraPaddleCounter;

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

    public void onCounterFinish() {
        this.removePaddleFunction.run(extraPaddle, Layer.DEFAULT);
        extraPaddleCounter.decrement();
    }

}
