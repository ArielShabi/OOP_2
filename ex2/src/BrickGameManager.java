
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import game.BricksManager;
import gameobjects.Ball;
import gameobjects.Paddle;

import java.awt.*;
import java.util.Random;


public class BrickGameManager extends GameManager {
    private static final float BALL_SPEED = 200;
    private static final float WALL_WIDTH = 10;
    private static final float BALL_RADIUS = 35;
    private static final int PADDLE_WIDTH = 150;
    private static final float PADDLE_HEIGHT = 20;
    private static final int DEFAULT_NUMBER_OF_BRICK_ROWS = 6;
    private static final int DEFAULT_NUMBER_OF_BRICKS_PER_ROW = 5;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private BricksManager bricksManager;


    public BrickGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();


        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg"
                , false);
        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions,
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);


        // Create walls
        Renderable wallRenderable = new RectangleRenderable(Color.pink);
        Vector2[] wallPositions = {Vector2.ZERO,
                new Vector2(this.windowDimensions.x() - WALL_WIDTH, 0)};

        for (int i = 0; i < 2; i++) {
            GameObject wall =
                    new GameObject(wallPositions[i], new Vector2(WALL_WIDTH,
                            this.windowDimensions.y()), wallRenderable);

            this.gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        }

        GameObject ceiling = new GameObject(Vector2.ZERO,
                new Vector2(this.windowDimensions.x(), WALL_WIDTH), wallRenderable);

        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);


        // Creating ball
        Renderable ballImage =
                imageReader.readImage("assets/ball.png", true);

        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        float ballVelocityX = BALL_SPEED;
        float ballVelocityY = BALL_SPEED;
        Random rand = new Random();

        if (rand.nextBoolean()) {
            ballVelocityX = -ballVelocityX;
        }
        if (rand.nextBoolean()) {
            ballVelocityY = -ballVelocityY;
        }

        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));
        Vector2 windowDimensions = this.windowDimensions;
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);


        // Creating paddles

        Renderable paddleImage =
                imageReader.readImage("assets/paddle.png", false);

        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);

        // create bricks
        Renderable brickRenderable = imageReader.readImage("assets/brick.png", false);

        bricksManager = new BricksManager(
                new Vector2(WALL_WIDTH, WALL_WIDTH),
                new Vector2(windowDimensions.x() - 2 * WALL_WIDTH, windowDimensions.y() - 2 * WALL_WIDTH),
                null, this::addStaticGameObject, this::removeStaticGameObject);

        gameObjects().addGameObject(bricksManager);

        bricksManager.createBricks(DEFAULT_NUMBER_OF_BRICKS_PER_ROW, DEFAULT_NUMBER_OF_BRICK_ROWS,
                brickRenderable);


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        checkForGameEnd();
    }

    private void checkForGameEnd() {
        float ballY = ball.getCenter().y();
        String prompt = "";
        if (ballY > this.windowDimensions.y()) {
            // finished
            prompt = "You lose!";
        }

        if (!bricksManager.hasBricks()) {
            prompt = "You win!";
        }

        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (this.windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }

    }

    private void addStaticGameObject(GameObject gameObject) {
        gameObjects().addGameObject(gameObject, Layer.STATIC_OBJECTS);
    }

    private void removeStaticGameObject(GameObject gameObject) {
        gameObjects().removeGameObject(gameObject, Layer.STATIC_OBJECTS);
    }

    public static void main(String[] args) {

        GameManager game = new BrickGameManager("Bouncing Ball", new Vector2(700, 500));

        game.run();

        System.out.println("Hello World" + game);
    }
}