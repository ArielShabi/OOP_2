package collected_strategy;

import danogl.GameObject;
import gameobjects.Collectable;

import java.util.function.Supplier;

public class HeartCollectedStrategy implements CollectedStrategy {
    private final Supplier<Integer> addHeartFunction;

    public HeartCollectedStrategy(Supplier<Integer> addHeartFunction) {
        this.addHeartFunction = addHeartFunction;
    }

    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        addHeartFunction.get();
    }

}
