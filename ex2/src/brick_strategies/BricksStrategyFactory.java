package brick_strategies;

import ball.BallFactory;
import danogl.gui.ImageReader;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Ball;

import java.util.Random;

public class BricksStrategyFactory {
    private final Random random;
    private final AddGameObjectFunction addBrickFunction;
    private final BallFactory ballFactory;
    private final ImageReader imageReader;
    private final Ball mainBall;

    public BricksStrategyFactory(
            AddGameObjectFunction addBrickFunction,
            BallFactory ballFactory, ImageReader imageReader,
            Ball mainBall) {
        this.addBrickFunction = addBrickFunction;
        this.ballFactory = ballFactory;
        this.imageReader = imageReader;
        this.mainBall = mainBall;

        random = new Random();
    }

    public CollisionStrategy generateCollisionStrategy(RemoveGameObjectFunction removeBrickFunction) {
        int chance = random.nextInt(10);

        if (isBetween(chance, 0, 5)) {
            return new BasicCollisionStrategy(removeBrickFunction);
        } else if (isBetween(chance, 5, 6)) {
            return new PuckCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    this.ballFactory);
        } else if (isBetween(chance, 6, 10)) {
            return new TurboCollisionStrategy(removeBrickFunction, this.mainBall, this.imageReader);
        } else {
            return null;
        }
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}
