package bricker.collected_strategy;

import danogl.GameObject;
import danogl.components.Component;
import bricker.gameobjects.Collectable;

/**
 * Strategy for when a hamburger is collected.
 */
public class HamburgerCollectedStrategy implements CollectedStrategy, Component {
    private static final int LARGE_SIZE_TIME = 5000;
    private static final float SIZE_MULTIPLIER = 1.5f;
    private long timeOnStart;
    private GameObject mainObj;

    /**
     * Method to be called when a hamburger is collected.
     * @param mainObj The object that collected the hamburger.
     * @param toCollect The hamburger that was collected.
     */
    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        this.mainObj = mainObj;
        mainObj.transform().setDimensions(mainObj.getDimensions().x() * SIZE_MULTIPLIER,
                mainObj.getDimensions().y());
        timeOnStart = System.currentTimeMillis();
        mainObj.addComponent(this);
    }

    /**
     * Update method for the component. Makes the object shrink back to its original size after time limit.
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        if (System.currentTimeMillis() - timeOnStart > LARGE_SIZE_TIME) {
            mainObj.transform().setDimensions(mainObj.getDimensions().x() / SIZE_MULTIPLIER,
                    mainObj.getDimensions().y());
            mainObj.removeComponent(this);
        }
    }
}
