package derezengine.scenes;

import derezengine.Camera;

public abstract class Scene {

    private boolean isRunning = false;
    protected Camera camera; //protected bc we only want the scenes to be able to access it
    //private List<GameObject> gameObjects;

    public Scene() {

    }

    public abstract void update(float dt);

    public void init() {

    }

    public void start() {
        //will loop thru all the gameObjects in the scene and start them, because this is something that is always consistent throughout scenes
        this.isRunning = true;

    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void saveExit() { //more for like SERIALIZATION

    }

    public void load() { //more for like DESERIALIZATION

    }

    public Camera getCamera() {
        return camera;
    }
}
