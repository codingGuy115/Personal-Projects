package derezengine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.joml.Math.sqrt;
import static org.joml.Math.toRadians;

public class Camera { //Gonna be ORTHOGRAPHIC

    private Matrix4f viewMatrix, projectionMatrix;
    private Matrix4f inverseView, inverseProjection;

    public Vector3f position; //lets make this public, it gets changed a lot
    public Vector3f cameraTarget;
    public float radius; //represents the distance of the position from the cameraTarget
    private Vector2f projectionSize = new Vector2f(2, 2); //im pretty sure we might be able to change the projection size if the window is resized??

    private float cameraSpeed = 5.0f;

    public Camera(Vector3f position) {
        this.position = position;
        this.cameraTarget = new Vector3f(0.0f, 0.0f, 0.0f);

        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.inverseView = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() { //if window is adjusted, we can make changes to the matrix and frustum here. (objects aren't resized yet).
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f);
        //projectionMatrix.perspective(toRadians(45.0f), projectionSize.x / projectionSize.y, 0.1f, 100.0f, projectionMatrix);
        projectionMatrix.invert(inverseProjection); //-> we are inverting the projectionMatrix into destination (inverseProjection)
    }

    public Matrix4f getViewMatrix() {
        /** REMEMBER: the view matrix transforms objects from world space to view space. If the camera's position is
         * often changing, the view matrix will change. we must account for this.
         */

        //** Think 2D sideScroller
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, position.z),    //eye-> is position of camera. (here its 20 units "out of screen")
                                        cameraFront.add(position.x, position.y, 0.0f), //center
                                        cameraUp);                                        //up

        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;


    }

    public Matrix4f getProjectionMatrix() {
        /**why is there not much work here? Bc the projection is view -> clip space,
         * and the clip calculations are done automatically by OpenGL. (plus we set up the projectionMatrix in adjustProjection()
         */
        return projectionMatrix;
    }

    public float getCameraSpeed() {
        return cameraSpeed;
    }

    public void setCameraSpeed(float speed) {
        this.cameraSpeed = speed;
    }
}