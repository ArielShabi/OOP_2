package bricker.collected_strategy;

import danogl.GameObject;
import bricker.gameobjects.Collectable;

public class HeartCollectedStrategy implements CollectedStrategy {
    private final Runnable addHeartFunction;

    public HeartCollectedStrategy(Runnable addHeartFunction) {
        this.addHeartFunction = addHeartFunction;
    }

    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        addHeartFunction.run();
    }

}