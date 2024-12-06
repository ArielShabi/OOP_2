package bricker.brick_strategies;

import bricker.ball.BallFactory;
import bricker.collected_strategy.HamburgerCollectedStrategy;
import bricker.collected_strategy.HeartCollectedStrategy;
import bricker.main.AssetsConfig;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.game.AddGameObjectFunction;
import bricker.game.RemoveGameObjectFunction;
import bricker.gameobjects.Ball;
import bricker.paddle.PaddleFactory;

import java.util.Random;
import java.util.function.Consumer;

public class BricksStrategyFactory {
    private static final int MAX_AMOUNT_OF_MULTI_COLLISIONS = 2;
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
        boolean toCreteBasicStrategy = random.nextBoolean();

        if (toCreteBasicStrategy) {
            return new BasicCollisionStrategy(removeBrickFunction);
        }

        return this.generateSpecialCollisionStrategy(removeBrickFunction,
                new Counter(MAX_AMOUNT_OF_MULTI_COLLISIONS));
    }

    public CollisionStrategy generateSpecialCollisionStrategy(Consumer<GameObject> removeBrickFunction,
                                                              Counter multiCollisionCounter) {
        boolean shouldIncludeMultiCollision = multiCollisionCounter.value() != 0;

        int chance = random.nextInt(CollisionStrategyType.values().length -
                (shouldIncludeMultiCollision ?
                        0 : 1));

        CollisionStrategyType strategyType = CollisionStrategyType.values()[chance];

        switch (strategyType) {
            case PUCK -> {
                return new PuckCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                        this.ballFactory);
            }
            case EXTRA_PADDLE -> {
                return new ExtraPaddleCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                        removeGameObjectFunction, this.paddleFactory,
                        this.windowDimensions, extraPaddleCounter);
            }
            case TURBO -> {
                return new TurboCollisionStrategy(removeBrickFunction, mainBall, imageReader);
            }
            case HEART_COLLECTABLE -> {
                return new CollectableCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                        removeGameObjectFunction, this.imageReader.readImage(AssetsConfig.HEART_PATH, true),
                        new HeartCollectedStrategy(addHeartFunction), collectorObject);
            }
            case HAMBURGER_COLLECTABLE -> {
                return new CollectableCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                        removeGameObjectFunction, this.imageReader.readImage(AssetsConfig.HAMBURGER_PATH, true),
                        new HamburgerCollectedStrategy(), collectorObject);
            }
            default -> {
                multiCollisionCounter.decrement();
                return new MultiCollisionStrategy(removeBrickFunction,
                        this.generateSpecialCollisionStrategy(removeBrickFunction, multiCollisionCounter),
                        this.generateSpecialCollisionStrategy(removeBrickFunction, multiCollisionCounter));
            }
        }
    }
}
