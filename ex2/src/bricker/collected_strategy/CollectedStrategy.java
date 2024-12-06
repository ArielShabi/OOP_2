package bricker.collected_strategy;

import danogl.GameObject;
import bricker.gameobjects.Collectable;

public interface CollectedStrategy {

    void collectOnCollision(GameObject mainObj, Collectable toCollect);
}
