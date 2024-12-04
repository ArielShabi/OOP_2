package gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import paddle.PaddleType;

import java.awt.event.KeyEvent;



public class Paddle extends GameObject {
    public static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    public PaddleType paddleType = PaddleType.Main;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels). Note that (0,0) is the
     *                      top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case the GameObject
     *                      will not be rendered.
     * @param inputListener The input listener that will be used to control the paddle.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener) {

        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDirection = Vector2.ZERO;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDirection = movementDirection.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDirection = movementDirection.add(Vector2.RIGHT);
        }


        if(getTopLeftCorner().x() <= 20){
            if(movementDirection.x() < 0){
                movementDirection = Vector2.ZERO;
            }
        } else if (this.getTopLeftCorner().x()+this.getDimensions().x()>= 700-20){
            if(movementDirection.x() > 0){
                movementDirection = Vector2.ZERO;
            }

        }

        this.setVelocity(movementDirection.mult(MOVEMENT_SPEED));
    }
}