package deimos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.*;

public class Game extends ComponentHolder {
    private static final Logger log = LoggerFactory.getLogger(Game.class);

    private Scene startScene;
    private Scene currentScene;
    private Map<String, Scene> scenes = new HashMap<>();

    public Scene addScene(String id, Reader entityConfig) {
        if (scenes.containsKey(id))
            throw new IllegalStateException("Duplicate Scene ID: " + id);

        Scene scene = new Scene(id, entityConfig);
        scenes.put(id, scene);
        if (startScene == null)
            startScene = scene;

        return scene;
    }

    public Scene addScene(String id) {
        return addScene(id, null);
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
            log.warn("No such scene ID: {}", id, new NullPointerException());
            return;
        }

        currentScene.unload();
        currentScene = scenes.get(id);
        currentScene.load(this);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}
