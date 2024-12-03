package brick_strategies;

import game.RemoveGameObjectFunction;

import java.util.Random;

public class BricksStrategyFactory {
    private final Random random;

    public BricksStrategyFactory(){
        random = new Random();
    }

    public CollisionStrategy generateCollisionStrategy(RemoveGameObjectFunction removeBrickFunction){
        int chance = random.nextInt(10);

        if(isBetween(chance, 0,5)){
            return new BasicCollisionStrategy(removeBrickFunction);
        }
        else{
            // other strategies
            return null;
        }
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x < upper;
    }
}
