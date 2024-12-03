package ball;

import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;

import java.util.Random;

public class BallFactory {
    private static final float BALL_RADIUS = 35;
    private static final float BALL_SPEED = 200;
    private static final float PUCK_RADIUS_MULTIPLIER = 0.75f;

    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Random rand;

    public BallFactory(ImageReader imageReader, SoundReader soundReader) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.rand = new Random();
    }

    public Ball createBall(BallType ballType) {
        Renderable ballImage =null;

        Sound collisionSound = soundReader.readSound("assets/blop.wav");

        float ballRadius = BALL_RADIUS;

        float ballVelocityX = BALL_SPEED;
        float ballVelocityY = BALL_SPEED;

        if (ballType == BallType.MAIN) {
            ballVelocityX *= rand.nextBoolean() ? 1 : -1;
            ballVelocityY *= rand.nextBoolean() ? 1 : -1;
            ballImage = imageReader.readImage("assets/ball.png", true);
        } else if (ballType == BallType.PUCK) {
            ballRadius *= PUCK_RADIUS_MULTIPLIER;
            double angle = rand.nextFloat() * Math.PI;
            ballVelocityX *= (float) (Math.cos(angle));
            ballVelocityY *= (float) (Math.sin(angle));
            ballImage = imageReader.readImage("assets/mockBall.png", true);
        }


        Ball ball = new Ball(Vector2.ZERO, new Vector2(ballRadius, ballRadius), ballImage, collisionSound);

        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));

        return ball;
    }
}
