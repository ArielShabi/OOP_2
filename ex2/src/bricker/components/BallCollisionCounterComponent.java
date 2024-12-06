package bricker.components;

import danogl.components.Component;
import bricker.gameobjects.Ball;

/**
 * Component that counts the number of collisions of a ball.
 */
public class BallCollisionCounterComponent implements Component {
    private final Ball owner;
    private final int endCount;
    private final Runnable onCounterFinish;

    /**
     * Constructor for the ball collision counter component.
     * @param owner The ball that owns the component.
     * @param endCount The number of collisions to count.
     * @param onCounterFinish The function to call when the counter finishes.
     */
    public BallCollisionCounterComponent(Ball owner, int endCount, Runnable onCounterFinish) {
        this.owner = owner;
        this.endCount = endCount;
        this.onCounterFinish = onCounterFinish;
    }

    /**
     * Updates the component.
     * @param deltaTime The time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        if(owner.getCollisionCounter() == endCount){
            this.onCounterFinish.run();
            owner.removeComponent(this);
        }
    }
}
