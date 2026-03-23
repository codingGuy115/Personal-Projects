package derezengine;

import derezengine.scenes.LevelEditorScene;
import derezengine.scenes.LevelScene;
import derezengine.scenes.Scene;
import derezengine.util.Settings;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.joml.Math.sin;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private String windowTitle;
    private int width;
    private int height;
    private static long glfwWindow;

    private static Window window = null; //object

    //private static Scene currentScene;


    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.windowTitle = "LWJGL sandbox";
    }

//    public void changeScene(int newScene)
//    {
//
//        switch (newScene)
//        {
//            case 0:
//                currentScene = new LevelEditorScene();
//                break;
//            case 1:
//                currentScene = new LevelScene();
//                break;
//            default:
//                assert false : "Unknown scene '" + newScene + "'";
//                break;
//        }
//
//        currentScene.load();
//        currentScene.init();
//        currentScene.start();
//    }




    public static Window get() { //this is for singleton functionality
        if (Window.window == null) { //means no object was created, (because of static), ->then create one
            window = new Window();
        }

        return Window.window;

    }

//    public static Scene getScene() {
//        return get().currentScene;
//    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
        /**at this point, GAMELOOP is over.**/

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //what needs to happen inside init() ?
        // Setup an error callback. The default implementation will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); //Makes sure when window is created, it is in maximized position

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.windowTitle, NULL, NULL);
        if ( glfwWindow == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. right now glfwWindow is collecting it all.
//        glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(glfwWindow, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    glfwWindow,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities(); //THIS IS SO FUCKING IMPORTANT THAT IT HAPPENS IN INIT STAGE. im so deadass
        /**LWJGL detects the context that is current in the current thread,
         * creates the GLCapabilities instance and makes the OpenGL bindings available for use.
         */
        // Enable v-sync
        glfwSwapInterval(1); //giving the window the number of screen updates to wait before switching the buffers in "swapBuffers()"

        // Make the window visible
        glfwShowWindow(glfwWindow);

        //--get a scene up and running--
        SceneManager.get().changeScene(0);

    }

    public static void loop() { //MAIN GAME LOOP
        //what needs to happen inside loop()? ->this is the delta time control stuff

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;


        // Run the rendering loop until the user has attempted to close window or press escape key
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            /**Do all the game shit**/
            // Poll for window events. The key callback above will only be invoked during this call.
            glfwPollEvents();
            // Set the clear color
            glClearColor(Settings.glfwWindowColor.x, Settings.glfwWindowColor.y, Settings.glfwWindowColor.z, Settings.glfwWindowColor.w);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            //we want to service our systems at this stage
            SceneManager.get().currentScene.update(dt);
            //System.out.println("FPS: "+ (1/dt));



            glfwSwapBuffers(glfwWindow); // swap the front and back buffers
            //System.out.println("FPS: "+ (1.0f/dt));
            /**now that game shit is over, get the dt value**/
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

    }

//    public Scene getCurrentScene() {
//        return this.currentScene;
//    }
}