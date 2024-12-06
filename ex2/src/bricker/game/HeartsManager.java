package bricker.game;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import bricker.main.Config;

import java.awt.*;

public class HeartsManager {
    private final static int INITIAL_HEARTS = 3;
    private int heartsCounter;
    private final int MAX_HEARTS = 4;
    private final GameObject[] hearts;
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final AddGameObjectFunction addGameObjectFunction;
    private final ImageReader imageReader;
    private final float HEART_SIZE = Config.HEART_SIZE;
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
        heartsCounter--;
        removeGameObjectFunction.run(hearts[this.heartsCounter], Layer.UI);
        hearts[this.heartsCounter]=null;
        updateNumericHearts();
    }

    public void addHeart() {
        if (heartsCounter < MAX_HEARTS) {
            Renderable heartRenderable = imageReader.readImage("assets/heart.png", true);
            GameObject heart = new GameObject(Vector2.ZERO, new Vector2(HEART_SIZE, HEART_SIZE),
                    heartRenderable);
            heart.setCenter(new Vector2(4*HEART_SIZE + this.heartsCounter * HEART_SIZE,this.windowDimensions.y()- HEART_SIZE));
            addGameObjectFunction.run(heart, Layer.UI);
            hearts[this.heartsCounter] = heart;
            heartsCounter++;
            updateNumericHearts();
        }
    }

    private void updateNumericHearts() {
        TextRenderable textRenderable = new TextRenderable(String.valueOf(heartsCounter));
        switch(heartsCounter){
            case 1:
                textRenderable.setColor(Color.RED);
                break;
            case 2:
                textRenderable.setColor(Color.YELLOW);
                break;
            default:
                textRenderable.setColor(Color.GREEN);
                break;
        }
        this.numericCount.renderer().setRenderable(textRenderable);
    }

    public int getHeartsCounter() {
        return heartsCounter;
    }

}
