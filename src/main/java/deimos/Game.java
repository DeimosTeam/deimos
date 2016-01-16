package deimos;

import java.util.*;

public class Game extends ComponentHolder {

    private Scene startScene;
    private Scene currentScene;
    private Map<String, Scene> scenes = new HashMap<>();

    public Scene addScene(String id) {
        if (scenes.containsKey(id))
            throw new IllegalStateException("Duplicate Scene ID: " + id);

        Scene scene = new Scene(id);
        scenes.put(id, scene);
        if (startScene == null)
            startScene = scene;

        return scene;
    }

    void load() throws Exception {
        initComponents(this, null, null);

        if (startScene != null) {
            currentScene = startScene;
            currentScene.load(this);
        }
    }

    public void switchScene(String id) {
        if (!scenes.containsKey(id)) {
            new NullPointerException("No such Scene ID: " + id).printStackTrace();
            return;
        }

        currentScene.unload();
        currentScene = scenes.get(id);
        currentScene.load(this);
    }
}
