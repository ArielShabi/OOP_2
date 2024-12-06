package bricker.brick_strategies;

import bricker.ball.BallFactory;
import bricker.ball.BallType;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import bricker.game.AddGameObjectFunction;
import bricker.gameobjects.Ball;
import java.util.function.Consumer;

/**
 * Collision strategy that creates two pucks when a collision occurs.
 */
public class PuckCollisionStrategy extends BasicCollisionStrategy {

    private final static int PUCK_TO_CREATE = 2;
    private final AddGameObjectFunction addGameObjectFunction;
    private final BallFactory ballFactory;

    /**
     * Constructor for the puck collision strategy.
     * @param removeGameObjectFunction The function to remove the game object.
     * @param addGameObjectFunction The function to add a game object.
     * @param ballFactory The factory for creating balls.
     */
    public PuckCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                 AddGameObjectFunction addGameObjectFunction, BallFactory ballFactory) {
        super(removeGameObjectFunction);
        this.addGameObjectFunction = addGameObjectFunction;
        this.ballFactory = ballFactory;
    }

    /**
     * Creates two pucks when a collision occurs.
     * @param a The first object in the collision.
     * @param b The second object in the collision.
     */
    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);

        float removedObjectWidth = a.getDimensions().x();
        for (int i = 0; i < PUCK_TO_CREATE; i++) {
            Ball ball = this.ballFactory.createBall(BallType.PUCK);

            // Space the pucks out, relative to the removed object width
            Vector2 puckPosition = new Vector2(a.getCenter().x() + removedObjectWidth / PUCK_TO_CREATE,
                    a.getCenter().y());

            ball.setCenter(puckPosition);
            this.addGameObjectFunction.run(ball, Layer.DEFAULT);
        }
    }
}
