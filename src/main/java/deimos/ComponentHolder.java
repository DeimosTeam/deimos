package deimos;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ComponentHolder {

    protected Map<Class<? extends Component>, Component> components = new LinkedHashMap<>();

    protected void loadComponents(Game game, Scene scene, Entity entity) throws Exception {
        for (Class<? extends Component> clazz : components.keySet()) {
            Component comp = Component.instantiate(clazz, game, scene ,entity);
            components.put(clazz, comp);
            Engine.newComponent(comp);
        }
    }

    public void addComponent(Class<? extends Component> component) {
        components.put(component, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> compClass) {
        return (T) components.get(compClass);
    }


}
