package game;

import brick_strategies.BasicCollisionStrategy;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Brick;

public class BricksManager extends GameObject {
    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_GAP = 2;
    private final AddGameObjectFunction addBrickFunction;
    private final RemoveGameObjectFunction removeBrickFunction;

    private Brick[] bricks;
    private int bricksCounter = 0;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     */
    public BricksManager(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                         AddGameObjectFunction addBrickFunction,
                         RemoveGameObjectFunction removeBrickFunction) {
        super(topLeftCorner, dimensions, renderable);

        this.addBrickFunction = addBrickFunction;
        this.removeBrickFunction = removeBrickFunction;
    }


    public void createBricks(int numberOfBricksPerRow, int numberOfBrickRows, Renderable brickRenderable) {
        this.bricks = new Brick[numberOfBricksPerRow * numberOfBrickRows];

        float brickWidth =
                (getDimensions().x() - ((numberOfBricksPerRow - 1) * BRICK_GAP)) / numberOfBricksPerRow;

        for (int i = 0; i < numberOfBrickRows; i++) {
            for (int j = 0; j < numberOfBricksPerRow; j++) {
                Brick brick = new Brick(
                        new Vector2(this.getTopLeftCorner().x() + j * (brickWidth + BRICK_GAP),
                                +this.getTopLeftCorner().y() + i * (BRICK_HEIGHT + BRICK_GAP)),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickRenderable,
                        new BasicCollisionStrategy(this::removeBrick)
                );

                bricks[i * numberOfBricksPerRow + j] = brick;
                bricksCounter++;
                addBrickFunction.run(brick);
            }
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    private void removeBrick(GameObject gameObject) {
        removeBrickFunction.run(gameObject);
        bricksCounter--;
    }

    public boolean hasBricks() {
        return bricksCounter > 0;
    }
}