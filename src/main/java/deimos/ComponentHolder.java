package deimos;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ComponentHolder {
    private Map<Class<? extends Component>, Consumer<Component>> initializers = new HashMap<>();
    protected Map<Class<? extends Component>, Component> components = new LinkedHashMap<>();

    protected void initComponents(Game game, Scene scene, Entity entity) {
        for (Class<? extends Component> clazz : initializers.keySet()) {
            try {
                Component comp = Component.instantiate(clazz, game, scene, entity);
                components.put(clazz, comp);
                Engine.initComponent(comp, initializers.get(clazz));
            } catch (Exception e) {
                String msg = String.format("Failed to instantiate %s: No nullary constructor was found.", clazz.getSimpleName());
                throw new IllegalStateException(msg);
            }
        }
    }

    protected void stopComponents() {
        components.values().stream()
                .forEach(Engine::stopComponent);
        components.clear();
    }

    public void addComponent(Class<? extends Component> component) {
        addComponent(component, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> void addComponent(Class<T> component, Consumer<T> init) {
        initializers.put(component, (Consumer<Component>)init);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> compClass) {
        return (T) components.get(compClass);
    }

    public boolean hasComponent(Class<? extends Component> component) {
        return initializers.containsKey(component);
    }

}
