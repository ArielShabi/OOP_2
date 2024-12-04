package brick_strategies;

import ball.BallFactory;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Paddle;
import paddle.PaddleFactory;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BricksStrategyFactory {
    private final Random random;
    private final AddGameObjectFunction addBrickFunction;
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final BallFactory ballFactory;
    private final PaddleFactory paddleFactory;
    private final Vector2 windowDimensions;
    private final Counter extraPaddleCounter;
    private final ImageReader imageReader;
    private Supplier<Integer> addHeartFunction;
    private final GameObject heartCollector;
    private final int HEART_SIZE;

    public BricksStrategyFactory(AddGameObjectFunction addBrickFunction,
                                 RemoveGameObjectFunction removeGameObjectFunction,
                                 BallFactory ballFactory, PaddleFactory paddleFactory,
                                 Vector2 windowDimensions, ImageReader imageReader, Supplier<Integer> addHeartFunction, GameObject heartCollector, int heartSize) {
        this.addBrickFunction = addBrickFunction;
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.ballFactory = ballFactory;
        this.paddleFactory = paddleFactory;
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.addHeartFunction = addHeartFunction;
        this.heartCollector = heartCollector;
        HEART_SIZE = heartSize;
        random = new Random();
        extraPaddleCounter = new Counter(0);


    }

    public CollisionStrategy generateCollisionStrategy(Consumer<GameObject> removeBrickFunction) {
        int chance = random.nextInt(10);

        if (isBetween(chance, 0, 5)) {
            return new BasicCollisionStrategy(removeBrickFunction);
        } else if (isBetween(chance, -2, -1)) {
            return new PuckCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    this.ballFactory);
        } else if (isBetween(chance, 5, 7)) {
            return new ExtraPaddleCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    removeGameObjectFunction, this.paddleFactory,
                    this.windowDimensions, extraPaddleCounter);
        } else if (isBetween(chance, 7, 10)) {
            return new HeartsCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    this.imageReader, removeGameObjectFunction, addHeartFunction, heartCollector, HEART_SIZE);
        } else {
            // other strategies
            return null;
        }
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}
