package derezengine;

import derezengine.scenes.LevelEditorScene;
import derezengine.scenes.LevelScene;
import derezengine.scenes.Scene;

public class SceneManager { //should probably be singleton //TODO: NOT NECESSARY atm

    private static SceneManager sceneManager = null;
    public Scene currentScene;

    public SceneManager() {

    }

    public static SceneManager get() {
        if (SceneManager.sceneManager == null) {
            sceneManager = new SceneManager();
        }

        return SceneManager.sceneManager;
    }

    public void changeScene(int newScene)
    {

        switch (newScene)
        {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }

        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

}
