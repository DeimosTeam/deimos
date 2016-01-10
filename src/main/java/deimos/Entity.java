package deimos;

public class Entity extends ComponentHolder {

    public Entity() {}

    // Constructor for cloning
    public Entity(Entity clone) {
        clone.components.keySet().forEach(this::addComponent);
    }

}
