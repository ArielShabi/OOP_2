package brick_strategies;

import ball.BallFactory;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;

import java.util.Random;

public class BricksStrategyFactory {
    private final Random random;
    private final AddGameObjectFunction addBrickFunction;
    private final BallFactory ballFactory;

    public BricksStrategyFactory(AddGameObjectFunction addBrickFunction,
                                 BallFactory ballFactory) {
        this.addBrickFunction = addBrickFunction;
        this.ballFactory = ballFactory;
        random = new Random();
    }

    public CollisionStrategy generateCollisionStrategy(RemoveGameObjectFunction removeBrickFunction) {
        int chance = random.nextInt(10);

        if (isBetween(chance, 0, 9)) {
            return new BasicCollisionStrategy(removeBrickFunction);
        } else if (isBetween(chance, 9, 10)) {
            return new PuckCollisionStrategy(removeBrickFunction, this.addBrickFunction,
                    this.ballFactory);
        } else {
            // other strategies
            return null;
        }
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}
