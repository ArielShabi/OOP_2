package collected_strategy;

import danogl.GameObject;
import danogl.components.Component;
import gameobjects.Collectable;

public class HamburgerCollectedStrategy implements CollectedStrategy, Component {
    public static final int LARGE_SIZE_TIME = 5000;
    private static final float SIZE_MULTIPLIER = 1.5f;
    private long timeOnStart;
    private GameObject mainObj;

    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        this.mainObj = mainObj;
        mainObj.transform().setDimensions(mainObj.getDimensions().x()*SIZE_MULTIPLIER, mainObj.getDimensions().y());
        timeOnStart = System.currentTimeMillis();
        mainObj.addComponent(this);
    }

    @Override
    public void update(float deltaTime) {
        if(System.currentTimeMillis() - timeOnStart > LARGE_SIZE_TIME) {
            mainObj.transform().setDimensions(mainObj.getDimensions().x()/SIZE_MULTIPLIER, mainObj.getDimensions().y());
            mainObj.removeComponent(this);
        }
    }
}
