package derezengine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener { //will use the singleton structure

    private static KeyListener instance;
    private static boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        }
        else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) { //NOTE: must implement special cases where keyCode is not in range. F10 key does not work for example.
        return get().keyPressed[keyCode];
    }

}