import ball.BallFactory;
import ball.BallType;
import brick_strategies.BricksStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import game.BricksManager;
import game.Config;
import game.HeartsManager;
import game.Utils;
import gameobjects.Ball;
import paddle.PaddleFactory;
import paddle.PaddleType;

import java.awt.*;
import java.awt.event.KeyEvent;


public class BrickGameManager extends GameManager {
    private static final int DEFAULT_NUMBER_OF_BRICK_ROWS = 6;
    private static final int DEFAULT_NUMBER_OF_BRICKS_PER_ROW = 5;
    private static final float DELETION_HEIGHT_THRESHOLD = 50;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private BricksManager bricksManager;
    private HeartsManager heartsManager;
    private UserInputListener inputListener;


    public BrickGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.heartsManager = new HeartsManager(this.gameObjects()::removeGameObject, this.gameObjects()::addGameObject,imageReader, windowDimensions);

        // Create background
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg"
                , false);
        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions,
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);


        // Create walls
        Renderable wallRenderable = new RectangleRenderable(Color.pink);
        Vector2[] wallPositions = {Vector2.ZERO,
                new Vector2(this.windowDimensions.x() - Config.WALL_WIDTH, 0)};

        for (int i = 0; i < 2; i++) {
            GameObject wall =
                    new GameObject(wallPositions[i], new Vector2(Config.WALL_WIDTH,
                            this.windowDimensions.y()), wallRenderable);

            this.gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        }

        GameObject ceiling = new GameObject(Vector2.ZERO,
                new Vector2(this.windowDimensions.x(), Config.WALL_WIDTH), wallRenderable);

        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);


        // Creating ball
        BallFactory ballFactory = new BallFactory(imageReader, soundReader);

        ball = ballFactory.createBall(BallType.MAIN);
        this.resetBallPosition();

        gameObjects().addGameObject(ball);

        // Creating paddle
        PaddleFactory paddleFactory = new PaddleFactory(imageReader, inputListener, windowDimensions);
        GameObject paddle = paddleFactory.createPaddle(PaddleType.Main);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);


        // create bricks
        Renderable brickRenderable = imageReader.readImage("assets/brick.png", false);

        BricksStrategyFactory bricksStrategyFactory = new BricksStrategyFactory(
                this.gameObjects()::addGameObject, this.gameObjects()::removeGameObject, ballFactory,
                paddleFactory, windowDimensions, imageReader, heartsManager::addHeart, paddle,
                this.ball);

        bricksManager = new BricksManager(
                new Vector2(Config.WALL_WIDTH, Config.WALL_WIDTH),
                new Vector2(windowDimensions.x() - 2 * Config.WALL_WIDTH, windowDimensions.y() - 2 * Config.WALL_WIDTH),
                this.gameObjects()::addGameObject,
                this.gameObjects()::removeGameObject,
                bricksStrategyFactory
        );

        bricksManager.createBricks(DEFAULT_NUMBER_OF_BRICKS_PER_ROW, DEFAULT_NUMBER_OF_BRICK_ROWS,
                brickRenderable);

        soundReader.readSound("assets/opening.wav").play();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        removeFallenItems();
    }

    private void removeFallenItems() {
        for (GameObject gameObject : gameObjects().objectsInLayer(Layer.DEFAULT)) {
            if (gameObject.getCenter().y() > this.windowDimensions.y() + DELETION_HEIGHT_THRESHOLD) {
                gameObjects().removeGameObject(gameObject);
            }
        }
    }

    private void checkForGameEnd() {
        float ballY = ball.getCenter().y();
        String prompt = "";
        if (ballY > this.windowDimensions.y()) {
            if (this.heartsManager.getHeartsCounter() - 1 > 0) {
                this.heartsManager.removeHeart();
                this.resetBallPosition();
                return;
            } else {
                prompt = "You lose!";
            }
        }

        if (this.checkForWin()) {
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

    private boolean checkForWin() {
        return !bricksManager.hasBricks() || this.inputListener.isKeyPressed(KeyEvent.VK_W);
    }


    private void resetBallPosition() {
        ball.setCenter(windowDimensions.mult(0.5f));
        Vector2 ballVelocity = Utils.getRandomDiagonal();

        ball.setVelocity(ballVelocity.mult(BallFactory.BALL_SPEED));
    }


    public static void main(String[] args) {

        GameManager game = new BrickGameManager("Bouncing Ball", new Vector2(700, 500));

        game.run();

        System.out.println("Hello World" + game);
    }
}