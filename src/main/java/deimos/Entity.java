package deimos;

import java.util.HashMap;

public class Entity extends ComponentHolder {

    public Entity() {}

    // Constructor for cloning
    public Entity(Entity source) {
        components = new HashMap<>(source.components);
    }

}
