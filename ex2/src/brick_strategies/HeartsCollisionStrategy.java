package brick_strategies;

import collected_strategy.CollectedStrategy;
import collected_strategy.HeartCollectedStrategy;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Collectable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class HeartsCollisionStrategy extends BasicCollisionStrategy {
    public static final float MOVEMENT_SPEED = 200;
    private final AddGameObjectFunction addGameObjectFunction;
    private final ImageReader imageReader;
    private final GameObject heartCollector;
    private final int HEART_SIZE;
    private final RemoveGameObjectFunction removeCollectableFunction;
    private final Supplier<Integer> addHeartFunction;


    public HeartsCollisionStrategy(Consumer<GameObject> removeGameObjectFunction,
                                   AddGameObjectFunction addGameObjectFunction, ImageReader imageReader,
                                   RemoveGameObjectFunction removeCollectableFunction,
                                   Supplier<Integer> addHeartFunction, GameObject heartCollector, int HEART_SIZE) {
        super(removeGameObjectFunction);
        this.addGameObjectFunction = addGameObjectFunction;
        this.removeCollectableFunction = removeCollectableFunction;
        this.addHeartFunction = addHeartFunction;
        this.imageReader = imageReader;
        this.heartCollector = heartCollector;
        this.HEART_SIZE = HEART_SIZE;
    }

    @Override
    public void onCollision(GameObject a, GameObject b) {
        super.onCollision(a, b);

        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        CollectedStrategy collectedStrategy = new HeartCollectedStrategy(removeCollectableFunction, addHeartFunction);
        Collectable heart = new Collectable(Vector2.ZERO, new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage, collectedStrategy, heartCollector);

        Vector2 heartPosition = new Vector2(a.getCenter().x(), a.getCenter().y()+HEART_SIZE); //to check without +HEART_SIZE
        heart.setCenter(heartPosition);
        heart.setVelocity(Vector2.DOWN.mult(MOVEMENT_SPEED));

        this.addGameObjectFunction.run(heart, Layer.DEFAULT);
    }

}
