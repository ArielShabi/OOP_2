package collected_strategy;

import danogl.GameObject;
import gameobjects.Collectable;

public interface CollectedStrategy {
    public void collectOnCollision(GameObject mainObj, Collectable toCollect);
}
