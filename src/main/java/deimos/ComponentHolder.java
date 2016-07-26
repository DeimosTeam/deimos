package deimos;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ComponentHolder {

    protected Map<Class<? extends Component>, Component> components = new LinkedHashMap<>();

    protected void initComponents(Game game, Scene scene, Entity entity) {
        for (Class<? extends Component> clazz : components.keySet()) {
            try {
                Component comp = Component.instantiate(clazz, game, scene, entity);
                components.put(clazz, comp);
                Engine.initComponent(comp);
            } catch (Exception e) {
                String msg = String.format("Failed to instantiate %s: No nullary constructor was found.", clazz.getSimpleName());
                throw new IllegalStateException(msg);
            }
        }
    }

    protected void stopComponents() {
        for (Class<? extends Component> clazz : components.keySet()) {
            Engine.stopComponent(components.get(clazz));
            components.put(clazz, null);
        }
    }

    public void addComponent(Class<? extends Component> component) {
        components.put(component, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> compClass) {
        return (T) components.get(compClass);
    }

    public boolean hasComponent(Class<? extends Component> component) {
        return components.containsKey(component);
    }

}
