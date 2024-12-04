package collected_strategy;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import game.AddGameObjectFunction;
import game.RemoveGameObjectFunction;
import gameobjects.Collectable;

import java.util.function.Supplier;

public class HeartCollectedStrategy implements CollectedStrategy {
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final AddGameObjectFunction addGameObjectFunction;
    private final Supplier<Integer> addHeartFunction;
    private final int HEART_SIZE;

    public HeartCollectedStrategy(RemoveGameObjectFunction removeGameObjectFunction,
                                  AddGameObjectFunction addGameObjectFunction, Supplier<Integer> addHeartFunction, int HEART_SIZE) {
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.addGameObjectFunction = addGameObjectFunction;
        this.addHeartFunction = addHeartFunction;
        this.HEART_SIZE = HEART_SIZE;
    }

    @Override
    public void collectOnCollision(GameObject mainObj, Collectable toCollect) {
        removeGameObjectFunction.run(toCollect, Layer.DEFAULT);
        int numOfHearts = addHeartFunction.get();
    }
    //GameObject heart = new GameObject()
//        addGameObjectFunction.run(, Layer.UI);

}
