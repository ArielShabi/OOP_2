package collected_strategy;

import danogl.GameObject;
import gameobjects.Collectable;

public interface CollectedStrategy {

    void collectOnCollision(GameObject mainObj, Collectable toCollect);
}
