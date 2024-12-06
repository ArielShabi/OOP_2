package bricker.main;

import bricker.ball.BallFactory;
import bricker.ball.BallType;
import bricker.brick_strategies.BricksStrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.game.BricksManager;
import bricker.game.Config;
import bricker.game.HeartsManager;
import bricker.game.Utils;
import bricker.gameobjects.Ball;
import bricker.paddle.PaddleFactory;
import bricker.paddle.PaddleType;

import java.awt.*;
import java.awt.event.KeyEvent;


public class BrickerGameManager extends GameManager {
    private static final int DEFAULT_NUMBER_OF_ROWS = 7;
    private static final int DEFAULT_NUMBER_OF_BRICKS_PER_ROW = 8;
    private static final float DELETION_HEIGHT_THRESHOLD = 30;
    public static final int ARGS_LENGTH = 2;
    private final int bricksPerRow;
    private final int brickRows;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private BricksManager bricksManager;
    private HeartsManager heartsManager;
    private UserInputListener inputListener;


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int brickRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        this(
                windowTitle,
                windowDimensions,
                DEFAULT_NUMBER_OF_BRICKS_PER_ROW,
                DEFAULT_NUMBER_OF_ROWS);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.heartsManager = new HeartsManager(this.gameObjects()::removeGameObject,
                this.gameObjects()::addGameObject, imageReader, windowDimensions);

        createBackground(imageReader);

        createWalls();

        BallFactory ballFactory = new BallFactory(imageReader, soundReader);
        createBall(ballFactory);

        PaddleFactory paddleFactory = new PaddleFactory(imageReader, inputListener, windowDimensions);
        GameObject paddle = createPaddle(paddleFactory);

        BricksStrategyFactory bricksStrategyFactory = new BricksStrategyFactory(
                this.gameObjects()::addGameObject, this.gameObjects()::removeGameObject, ballFactory,
                paddleFactory, windowDimensions, imageReader, heartsManager::addHeart, paddle,
                this.ball);

        createBricks(imageReader, soundReader, bricksStrategyFactory);

        soundReader.readSound("assets/opening.wav").play();
    }

    private void createBricks(ImageReader imageReader, SoundReader soundReader, BricksStrategyFactory bricksStrategyFactory) {
        Renderable brickRenderable = imageReader.readImage("assets/brick.png", false);

        bricksManager = new BricksManager(
                new Vector2(Config.WALL_WIDTH, Config.WALL_WIDTH),
                new Vector2(windowDimensions.x() - 2 * Config.WALL_WIDTH,
                        windowDimensions.y() - 2 * Config.WALL_WIDTH),
                this.gameObjects()::addGameObject,
                this.gameObjects()::removeGameObject,
                bricksStrategyFactory
        );

        bricksManager.createBricks(this.bricksPerRow, this.brickRows, brickRenderable);
    }

    private GameObject createPaddle(PaddleFactory paddleFactory) {
        GameObject paddle = paddleFactory.createPaddle(PaddleType.Main);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);
        return paddle;
    }

    private void createBall(BallFactory ballFactory) {
        ball = ballFactory.createBall(BallType.MAIN);
        this.resetBallPosition();

        gameObjects().addGameObject(ball);
    }

    private void createWalls() {
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
    }

    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg"
                , false);
        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions,
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.checkForGameEnd();
        this.removeFallenItems();
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

        int[] parsedArgs = parseArgs(args);
        GameManager game;
        if (parsedArgs != null) {
            game = new BrickerGameManager(
                    "Bouncing Ball",
                    new Vector2(700, 500),
                    parsedArgs[0],
                    parsedArgs[1]
            );
        } else {
            game = new BrickerGameManager("Bouncing Ball", new Vector2(700, 500));
        }


        game.run();


    }

    private static int[] parseArgs(String[] args) {
        if (args.length != ARGS_LENGTH) {
            return null;
        }
        int[] parsedArgs = new int[ARGS_LENGTH];

        for (int i = 0; i < ARGS_LENGTH; i++) {
            try {
                parsedArgs[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return parsedArgs;
    }
}