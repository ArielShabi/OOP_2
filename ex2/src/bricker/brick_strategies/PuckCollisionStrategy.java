package bricker.brick_strategies;

import bricker.ball.BallFactory;
import bricker.ball.BallType;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import bricker.game.AddGameObjectFunction;
import bricker.gameobjects.Ball;

import java.util.function.Consumer;

public class PuckCollisionStrategy extends BasicCollisionStrategy {

    private final static int PUCK_TO_CREATE = 2;
    private final AddGameObjectFunction addGameObjectFunction;
    private final BallFactory ballFactory;

    public PuckCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                 AddGameObjectFunction addGameObjectFunction, BallFactory ballFactory) {
        super(removeGameObjectFunction);
        this.addGameObjectFunction = addGameObjectFunction;
        this.ballFactory = ballFactory;
    }

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