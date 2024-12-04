package game;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class HeartsManager {
    private final int INITIAL_HEARTS = 3;
    private int heartscounter;
    private final int MAX_HEARTS = 4;
    private final GameObject[] hearts;
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final AddGameObjectFunction addGameObjectFunction;
    private final ImageReader imageReader;
    private final float HEART_SIZE = 20;
    private final Vector2 windowDimensions;
    private final GameObject numericCount;



    public HeartsManager(RemoveGameObjectFunction removeGameObjectFunction, AddGameObjectFunction addGameObjectFunction, ImageReader imageReader, Vector2 windowDimensions) {
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.addGameObjectFunction = addGameObjectFunction;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.hearts = new GameObject[MAX_HEARTS];

        GameObject numericCount = new GameObject(Vector2.ZERO, new Vector2(HEART_SIZE,HEART_SIZE),new TextRenderable(""));
        numericCount.setCenter(new Vector2(2*HEART_SIZE ,this.windowDimensions.y()- HEART_SIZE));
        addGameObjectFunction.run(numericCount, Layer.UI);
        this.numericCount = numericCount;

        for (int i = 0; i < INITIAL_HEARTS; i++) {
            addHeart();
        }
    }

    public void removeHeart() {
        heartscounter--;
        removeGameObjectFunction.run(hearts[this.heartscounter], Layer.UI);
        hearts[this.heartscounter]=null;
        updateNumericHearts();
    }

    public int addHeart() {
        if (heartscounter < MAX_HEARTS) {
            Renderable heartRenderable = imageReader.readImage("assets/heart.png", true);
            GameObject heart = new GameObject(Vector2.ZERO, new Vector2(HEART_SIZE, HEART_SIZE),
                    heartRenderable);
            heart.setCenter(new Vector2(4*HEART_SIZE + this.heartscounter * HEART_SIZE,this.windowDimensions.y()- HEART_SIZE));
            addGameObjectFunction.run(heart, Layer.UI);
            hearts[this.heartscounter] = heart;
            heartscounter++;
            updateNumericHearts();
        }
        return heartscounter;
    }

    private void updateNumericHearts() {
        // Added colors
        TextRenderable textRenderable = new TextRenderable(String.valueOf(heartscounter));
        switch(heartscounter){
            case 1:
                textRenderable.setColor(Color.RED);
                break;
            case 2:
                textRenderable.setColor(Color.YELLOW);
                break;
            case 3:
            case 4:
                textRenderable.setColor(Color.GREEN);
                break;
        }
        this.numericCount.renderer().setRenderable(textRenderable);
    }

    public int getHeartscounter() {
        return heartscounter;
    }

}
