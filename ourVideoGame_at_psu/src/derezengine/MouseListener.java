/**
 * THIS IS NOT MY CODE. I JUST NEEDED A MOUSELISTENER UP AND RUNNING FOR NOW.**/

package derezengine;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener
{
    private static MouseListener instance; //singleton of the mouse
    private double scrollX;
    private double scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[9]; //will store last button on mouse pressed
    private boolean isDragging;

    private MouseListener()
    {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;


    }
    // this get() function is same as window.get() bc both implement singleton logic
    public static MouseListener get()
    {
        if (MouseListener.instance == null)
        {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos)
    {
        get().lastX = get().xPos;
        get().lastY = get().yPos; //we are resetting the variables
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
        // ^this^ says 'if any of the mouse buttons are pressed and mouse just moved, user is DRAGGING something
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) { //this checks to make sure number of buttons on mouse being used does not exceed max in the array.
                get().mouseButtonPressed[button] = true;
            }
        }
        else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }

    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame()
    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }



    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

//    public static float getOrthoX() {
//        float currentX = getX();
//        currentX = (currentX / (float)Window.getWidth()) * 2.0f - 1; //should convert to a [-1, 1] range
//        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
//        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView()); //undoes operations to get to world coords for currentX
//        currentX = tmp.x;
//        //System.out.println(currentX); //-> this should be like -250 to whatever the 'right' val is for projectionMatrix.orth()
//
//        return currentX;
//    }
//
//    public static float getOrthoY() {
//        float currentY = Window.getHeight() - getY(); //Y coords are flipped from the way we project it to how we get in Screen coords
//        currentY = (currentY / (float)Window.getHeight()) * 2.0f -1; //should convert to a [-1, 1] range
//        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
//        tmp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView()); //undoes operations to get to world coords for currentX
//        currentY = tmp.y;
//
//        return currentY;
//    }

    //this method will give us the amount of elapsed x-position in the current frame
    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        else {
            return false;
        }
    }
}