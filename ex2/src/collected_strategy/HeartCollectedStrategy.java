package collected_strategy;

import danogl.GameObject;
import danogl.collisions.Layer;
import game.RemoveGameObjectFunction;
import gameobjects.Collectable;

import java.util.function.Supplier;

public class HeartCollectedStrategy implements CollectedStrategy {
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final Supplier<Integer> addHeartFunction;

    public HeartCollectedStrategy(RemoveGameObjectFunction removeGameObjectFunction, Supplier<Integer> addHeartFunction) {
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.addHeartFunction = addHeartFunction;
    }

    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        removeGameObjectFunction.run(toCollect, Layer.DEFAULT);
        addHeartFunction.get();
    }

}
