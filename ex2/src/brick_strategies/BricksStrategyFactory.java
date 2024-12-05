package brick_strategies;

import ball.BallFactory;
import collected_strategy.HamburgerCollectedStrategy;
import collected_strategy.HeartCollectedStrategy;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Ball;
import paddle.PaddleFactory;
import java.util.Random;
import java.util.function.Consumer;

public class BricksStrategyFactory {
    private final Random random;
    private final AddGameObjectFunction addBrickFunction;
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final BallFactory ballFactory;
    private final PaddleFactory paddleFactory;
    private final Vector2 windowDimensions;
    private final Counter extraPaddleCounter;
    private final ImageReader imageReader;
    private final Runnable addHeartFunction;
    private final GameObject collectorObject;
    private final Ball mainBall;

    public BricksStrategyFactory(AddGameObjectFunction addBrickFunction,
                                 RemoveGameObjectFunction removeGameObjectFunction,
                                 BallFactory ballFactory, PaddleFactory paddleFactory,
                                 Vector2 windowDimensions, ImageReader imageReader,
                                 Runnable addHeartFunction, GameObject collectorObject,
                                 Ball mainBall) {
        this.addBrickFunction = addBrickFunction;
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.ballFactory = ballFactory;
        this.paddleFactory = paddleFactory;
        this.windowDimensions = windowDimensions;
        this.imageReader = imageReader;
        this.addHeartFunction = addHeartFunction;
        this.collectorObject = collectorObject;
        this.mainBall = mainBall;
        random = new Random();
        extraPaddleCounter = new Counter(0);


    }

    public CollisionStrategy generateCollisionStrategy(Consumer<GameObject> removeBrickFunction) {
        int chance = random.nextInt(10);

        if (isBetween(chance, 0, 1)) {
            return new BasicCollisionStrategy(removeBrickFunction);
        } else if (isBetween(chance, 5, 6)) {
            return new PuckCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    this.ballFactory);
        } else if (isBetween(chance, 6, 7)) {
            return new ExtraPaddleCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    removeGameObjectFunction, this.paddleFactory,
                    this.windowDimensions, extraPaddleCounter);
        } else if (isBetween(chance, 7, 8)) {
            return new TurboCollisionStrategy(removeBrickFunction, mainBall, imageReader);
        } else if (isBetween(chance, 8, 9)) {
            return new CollectableCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    removeGameObjectFunction, this.imageReader.readImage("assets/heart.png", true),
                    new HeartCollectedStrategy(addHeartFunction), collectorObject);
        } else if (isBetween(chance, 1, 10)) {
            return new CollectableCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    removeGameObjectFunction, this.imageReader.readImage("assets/hamburger.png", true),
                    new HamburgerCollectedStrategy(), collectorObject);
        }
        return null;
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}
