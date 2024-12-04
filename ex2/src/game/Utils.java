package game;

import danogl.util.Vector2;

import java.util.Random;

public class Utils {
    public static Vector2 getRandomDiagonal() {
        Random rand = new Random();
        float ballVelocityX = rand.nextBoolean() ? 1 : -1;
        float ballVelocityY = rand.nextBoolean() ? 1 : -1;
        return new Vector2(ballVelocityX, ballVelocityY);
    }
}
