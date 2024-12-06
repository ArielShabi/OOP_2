package bricker.brick_strategies;

import bricker.components.BallCollisionCounterComponent;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import bricker.gameobjects.Ball;

import java.util.function.Consumer;

public class TurboCollisionStrategy extends BasicCollisionStrategy {
    private final Ball ballToTurbo;
    private static final int HIT_COUNT = 6;
    private static final float TURBO_SPEED = 1.4f;
    private final Renderable turboRenderable;
    private Renderable prevRenderable;

    public TurboCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                  Ball ballToTurbo, ImageReader imageReader) {
        super(removeGameObjectFunction);
        turboRenderable = imageReader.readImage("assets/redball.png", true);
        this.ballToTurbo = ballToTurbo;
    }

    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);

        if (b == ballToTurbo && !isAlreadyTurbo(b)) {
            prevRenderable = ballToTurbo.renderer().getRenderable();

            ballToTurbo.renderer().setRenderable(turboRenderable);

            ballToTurbo.addComponent(
                    new BallCollisionCounterComponent(
                            ballToTurbo,
                            ballToTurbo.getCollisionCounter() + HIT_COUNT,
                            this::onCounterFinish
                    ));
            ballToTurbo.setVelocity(b.getVelocity().mult(TURBO_SPEED));
        }
    }

    private void onCounterFinish() {
        ballToTurbo.setVelocity(ballToTurbo.getVelocity().mult(1 / TURBO_SPEED));
        ballToTurbo.renderer().setRenderable(prevRenderable);
    }

    private boolean isAlreadyTurbo(GameObject b) {
        return b.renderer().getRenderable() == turboRenderable;
    }
}
