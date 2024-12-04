package game;

import danogl.GameObject;
import gameobjects.ExtraPaddle;

public interface RemoveGameObjectFunction {
    boolean run(GameObject gameObject, int layer);
}
