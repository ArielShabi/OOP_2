package bricker.collected_strategy;

import danogl.GameObject;
import bricker.gameobjects.Collectable;

/**
 * Strategy for when a heart is collected.
 */
public class HeartCollectedStrategy implements CollectedStrategy {
    private final Runnable addHeartFunction;

    /**
     * Constructor for the strategy.
     * @param addHeartFunction The function to add a heart.
     */
    public HeartCollectedStrategy(Runnable addHeartFunction) {
        this.addHeartFunction = addHeartFunction;
    }

    /**
     * Method to be called when a heart is collected.
     * @param mainObj The object that collected the heart.
     * @param toCollect The heart that was collected.
     */
    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        addHeartFunction.run();
    }

}
