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
    private boolean rootNode = true;

    public Entity(String id) {
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    public Entity(String id, JsonObject initialConfig) {
        this(id);

        // Add components.
        for (Map.Entry<String, JsonElement> entry : initialConfig.entrySet()) {
            String componentId = entry.getKey();

            if (componentId.equals("children") || componentId.equals("root")) // TODO Maybe separate component array in JSON?
                continue;

            try {
                Class<?> clazz = getClass().getClassLoader().loadClass(componentId);

                if (!Component.class.isAssignableFrom(clazz)) {
                    log.warn("Not a component class in config");
                    continue;
                }
                Class<Component> cclazz = (Class<Component>)clazz;
                addComponent(cclazz, ConfigLoader.createConfigLoader(cclazz, initialConfig));

            } catch (ClassNotFoundException e) {
                log.warn("Component '{}' not found", componentId, e);
            }
        }

        // Set root node flag.
        JsonElement root = initialConfig.get("root");
        rootNode = root != null && root.getAsBoolean();
    }

    /**
     * Adds the entities referenced by ID in the configuration to the list of children.
     * Adds components from the configuration.
     *
     * TODO Figure out how a addChild method should be implemented.
     *
     * @param config JSON config element for the this entity.
     * @param batch Map storing the entity instances loaded from the configuration.
     */
    void addChildrenFromConfig(JsonObject config, Map<String, Entity> batch) {
        JsonElement childrenElm = config.get("children");
        if (childrenElm != null) {
            for (JsonElement elm : childrenElm.getAsJsonArray()) {
                String childId = elm.getAsString();

                children.add(batch.get(childId));
            }
        }
    }

    public boolean isRootNode() {
        return rootNode;
    }

    // Constructor for cloning
    public Entity(Entity source) {
        components = new HashMap<>(source.components);
        id = source.id + "_copy"; // TODO What to do when the other ID already exists?
        rootNode = source.rootNode;
    }

    public String getId() {
        return id;
    }

    @Override
    public Iterator<Entity> iterator() {
        return children.iterator();
    }
}
