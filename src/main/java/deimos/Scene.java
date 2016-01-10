package deimos;

import java.util.ArrayList;
import java.util.List;

public class Scene extends ComponentHolder {

    private final String id;
    private List<Entity> initialEntities;
    private List<Entity> entities;

    public Scene(String id) {
        this.id = id;
        initialEntities = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void load(Game game) throws Exception {
        loadComponents(game, this, null);

        // Load Entities
        for (Entity prefab : initialEntities) {
            Entity entity = new Entity(prefab);
            entity.loadComponents(game, this, entity);
            entities.add(entity);
        }
    }

    public Entity addEntity() {
        Entity entity = new Entity();
        initialEntities.add(entity);
        return entity;
    }
}
