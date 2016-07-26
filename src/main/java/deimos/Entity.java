package deimos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Entity extends ComponentHolder implements Iterable<Entity> {
    private static final Logger log = LoggerFactory.getLogger(Entity.class);

    private final Set<Entity> children = new LinkedHashSet<>();
    private String id;
    private JsonObject initialConfig;

    public Entity(String id) {
        this.id = id;
    }

    public Entity(String id, JsonObject initialConfig) {
        this(id);
        this.initialConfig = initialConfig;
    }

    /**
     * Adds the entities referenced by ID in the configuration to the list of children.
     * Adds components from the configuration.
     *
     * @param batch Map storing the entity instances loaded from the configuration.
     */
    @SuppressWarnings("unchecked")
    void prepareConfig(Map<String, Entity> batch) {
        if (initialConfig == null)
            return;

        // Add children.
        JsonElement children = initialConfig.get("children");
        if (children != null) {
            for (JsonElement elm : children.getAsJsonArray()) {
                String childId = elm.getAsString();

                this.children.add(batch.get(childId));
            }
        }

        // Add components.
        for (Map.Entry<String, JsonElement> entry : initialConfig.entrySet()) {
            String componentId = entry.getKey();
            if (componentId.equals("children") || componentId.equals("root")) // TODO Maybe separate component array?
                continue;

            try {
                Class<?> clazz = getClass().getClassLoader().loadClass(componentId);

                if (Component.class.isAssignableFrom(clazz))
                    addComponent((Class<? extends Component>)clazz);
                else
                    log.warn("Not a component class in config");
            } catch (ClassNotFoundException e) {
                log.warn("Component '{}' not found", componentId, e);
            }
        }
    }

    public boolean isRootNode() {
        if (initialConfig == null)
            return true; // TODO Handle when addChild is supported.

        JsonElement root = initialConfig.get("root");
        return root != null && root.getAsBoolean();
    }

    // Constructor for cloning
    public Entity(Entity source) {
        components = new HashMap<>(source.components);
        id = source.id + "_copy"; // TODO What to do when the other ID already exists?
        initialConfig = source.initialConfig;
    }

    public String getId() {
        return id;
    }

    public JsonObject getInitialConfigFor(Class<? extends Component> component) {
        if (initialConfig == null)
            return null;
        return initialConfig.get(component.getName()).getAsJsonObject();
    }

    public void addChild(Entity entity) {
        // TODO needs to be added to the list of scene entities.
        children.add(entity);
    }

    @Override
    public Iterator<Entity> iterator() {
        return children.iterator();
    }
}
