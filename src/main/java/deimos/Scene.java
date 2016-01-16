package deimos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Scene extends ComponentHolder {

    private final String id;
    private List<Entity> initialEntities;
    private List<Entity> liveEntities;

    public Scene(String id) {
        this.id = id;
        initialEntities = new ArrayList<>();
        liveEntities = new ArrayList<>();
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

    public Entity addEntity() {
        Entity entity = new Entity();
        initialEntities.add(entity);
        return entity;
    }

    public List<Entity> getEntitiesWith(Class<? extends Component> component) {
        return liveEntities.stream()
                .filter(e -> e.hasComponent(component))
                .collect(Collectors.toList());
    }
}
