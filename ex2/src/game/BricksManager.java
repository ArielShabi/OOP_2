package game;

import brick_strategies.BricksStrategyFactory;
import brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Brick;

public class BricksManager {
    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_GAP = 2;
    private final AddGameObjectFunction addBrickFunction;
    private final RemoveGameObjectFunction removeGameObjectFunction;
    private final BricksStrategyFactory bricksStrategyFactory;
    private final Vector2 topLeftCorner;
    private final Vector2 bricksContainerDimensions;

    private Brick[] bricks;
    private int bricksCounter = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixel s). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public BricksManager(Vector2 topLeftCorner, Vector2 dimensions,
                         AddGameObjectFunction addBrickFunction,
                         RemoveGameObjectFunction removeGameObjectFunction,
                         BricksStrategyFactory bricksStrategyFactory) {
        this.topLeftCorner = topLeftCorner;
        this.bricksContainerDimensions = dimensions;
        this.addBrickFunction = addBrickFunction;
        this.removeGameObjectFunction = removeGameObjectFunction;
        this.bricksStrategyFactory = bricksStrategyFactory;
    }


    public void createBricks(int numberOfBricksPerRow, int numberOfBrickRows, Renderable brickRenderable) {
        this.bricks = new Brick[numberOfBricksPerRow * numberOfBrickRows];

        float brickWidth =
                (this.bricksContainerDimensions.x() - ((numberOfBricksPerRow - 1) * BRICK_GAP)) / numberOfBricksPerRow;

        for (int i = 0; i < numberOfBrickRows; i++) {
            for (int j = 0; j < numberOfBricksPerRow; j++) {
                CollisionStrategy brickCollisionStrategy =
                        this.bricksStrategyFactory.generateCollisionStrategy(this::removeBrick);

                Brick brick = new Brick(
                        new Vector2(this.topLeftCorner.x() + j * (brickWidth + BRICK_GAP),
                                this.topLeftCorner.y() + i * (BRICK_HEIGHT + BRICK_GAP)),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickRenderable,
                        brickCollisionStrategy
                        );

                bricks[i * numberOfBricksPerRow + j] = brick;
                bricksCounter++;
                addBrickFunction.run(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    private boolean removeBrick(GameObject gameObject) {
        boolean removed = removeGameObjectFunction.run(gameObject, Layer.STATIC_OBJECTS);

        if (removed) {
            bricksCounter--;
        }

        return removed;
    }

    public boolean hasBricks() {
        return bricksCounter > 0;
    }
}
