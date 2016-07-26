package deimos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scene extends ComponentHolder {

    private final String id;
    private List<Entity> initialEntities;
    private List<Entity> liveEntities;

    public Scene(String id, Reader entityConfig) {
        this.id = id;
        initialEntities = new ArrayList<>();
        liveEntities = new ArrayList<>();

        if (entityConfig == null) // Empty scene with no configuration.
            return;

        JsonElement root = new JsonParser().parse(entityConfig);
        Map<String, Entity> staticEntities = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : ((JsonObject)root).entrySet()) {
            String eid = entry.getKey();
            Entity entity = new Entity(eid, (JsonObject)entry.getValue());

            Entity old = staticEntities.put(eid, entity);
            if (old != null)
                throw new IllegalStateException("Duplicate entity IDs: " + eid);

            initialEntities.add(entity);
        }

        // Add children and initial components. Defer to here such that all IDs
        // referenced in children are present in the staticEntites map.
        initialEntities.stream()
                .forEach(e -> e.prepareConfig(staticEntities));
    }

    public String getId() {
        return id;
    }

    public void load(Game game) {
        // Load components attached to scene
        initComponents(game, this, null);

        // Load Entities
        for (Entity entity : initialEntities) {
            entity.initComponents(game, this, entity);
            liveEntities.add(entity);
        }
    }

    public void unload() {
        stopComponents();
        liveEntities.forEach(Entity::stopComponents);
        liveEntities.clear();
    }

    public Entity addEntity(String id) {
        Entity entity = new Entity(id);
        initialEntities.add(entity);
        return entity;
    }

    public List<Entity> getEntitiesWith(Class<? extends Component> component) {
        return liveEntities.stream()
                .filter(e -> e.hasComponent(component))
                .collect(Collectors.toList());
    }
}
