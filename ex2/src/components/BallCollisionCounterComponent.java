package components;

import danogl.components.Component;
import gameobjects.Ball;

public class BallCollisionCounterComponent implements Component {
    private final Ball owner;
    private final int endCount;
    private final Runnable onCounterFinish;

    public BallCollisionCounterComponent(Ball owner, int endCount, Runnable onCounterFinish) {
        this.owner = owner;
        this.endCount = endCount;
        this.onCounterFinish = onCounterFinish;
    }

    @Override
    public void update(float deltaTime) {
        if(owner.getCollisionCounter() == endCount){
            this.onCounterFinish.run();
            owner.removeComponent(this);
        }
    }
}
