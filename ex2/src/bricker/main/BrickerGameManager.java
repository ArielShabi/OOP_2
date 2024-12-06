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
import bricker.game.HeartsManager;
import bricker.game.Utils;
import bricker.gameobjects.Ball;
import bricker.paddle.PaddleFactory;
import bricker.paddle.PaddleType;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Class that manages the bricker game.
 */
public class BrickerGameManager extends GameManager {
    private static final int DEFAULT_NUMBER_OF_ROWS = 7;
    private static final int DEFAULT_NUMBER_OF_BRICKS_PER_ROW = 8;
    private static final float DELETION_HEIGHT_THRESHOLD = 30;
    private static final int ARGS_LENGTH = 2;
    private final int bricksPerRow;
    private final int brickRows;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private BricksManager bricksManager;
    private HeartsManager heartsManager;
    private UserInputListener inputListener;
    private static final int PADDLE_DISTANCE_FROM_FLOOR = 30;
    private static final String GAME_NAME = "Bouncing Ball";
    private static final String LOSE_PROMPT = "You lose!";
    private static final String WIN_PROMPT = "You win!";
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final float WINDOWX_X_DIMENSIONS = 1500;
    private static final float WINDOWX_Y_DIMENSIONS = 800;

    /**
     * Construct a new BrickerGameManager instance.
     *
     * @param windowTitle       Title of the window.
     * @param windowDimensions  Width and height of the window.
     * @param bricksPerRow      Number of bricks per row.
     * @param brickRows         Number of brick rows.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int brickRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
    }

    /**
     * Construct a new BrickerGameManager instance. Uses default values for bricks per row and brick rows.
     *
     * @param windowTitle      Title of the window.
     * @param windowDimensions Width and height of the window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        this(
                windowTitle,
                windowDimensions,
                DEFAULT_NUMBER_OF_BRICKS_PER_ROW,
                DEFAULT_NUMBER_OF_ROWS);
    }

    /**
     * Initialize the game.
     *
     * @param imageReader      ImageReader instance.
     * @param soundReader      SoundReader instance.
     * @param inputListener    UserInputListener instance.
     * @param windowController WindowController instance.
     */
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

        createBricks(imageReader, bricksStrategyFactory);

        soundReader.readSound(AssetsConfig.OPENING_PATH).play();
    }

    /**
     * Update the game state. Check for game end and remove fallen items.
     *
     * @param deltaTime Time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.checkForGameEnd();
        this.removeFallenItems();
    }

    private void createBricks(ImageReader imageReader, BricksStrategyFactory bricksStrategyFactory) {
        Renderable brickRenderable = imageReader.readImage(AssetsConfig.BRICK_PATH, false);

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
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - PADDLE_DISTANCE_FROM_FLOOR));
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
        Renderable backgroundImage = imageReader.readImage(AssetsConfig.BACKGROUND_PATH
                , false);
        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions,
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
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
                prompt = LOSE_PROMPT;
            }
        }

        if (this.checkForWin()) {
            prompt = WIN_PROMPT;
        }

        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_PROMPT;
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


    /**
     * Main method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        int[] parsedArgs = parseArgs(args);
        GameManager game;
        if (parsedArgs != null) {
            game = new BrickerGameManager(
                    GAME_NAME,
                    new Vector2(WINDOWX_X_DIMENSIONS, WINDOWX_Y_DIMENSIONS),
                    parsedArgs[0],
                    parsedArgs[1]
            );
        } else {
            game = new BrickerGameManager(GAME_NAME, new Vector2(WINDOWX_X_DIMENSIONS, WINDOWX_Y_DIMENSIONS));
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