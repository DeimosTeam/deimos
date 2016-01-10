package deimos;

import java.util.*;

public class Game extends ComponentHolder {

    private Scene startScene;
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

    public void load() throws Exception {
        loadComponents(this, null, null);

        if (startScene != null)
            startScene.load();
    }

}
