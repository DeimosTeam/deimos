package deimos;

import com.google.gson.JsonObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for loading JSON configuration into a component instance.
 */
public class ConfigLoader {
    List<Setter> setters = new ArrayList<>();

    public ConfigLoader(Class<? extends Component> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Property prop = field.getAnnotation(Property.class);
            if (prop == null)
                continue;

            field.setAccessible(true);
            Class<?> T = field.getType();
            String name = field.getName();

            Setter setter;

            if (T.equals(int.class) || T.equals(Integer.class)) {
                setter = (c, j) -> field.set(c, j.get(name).getAsInt());
            } else {
                throw new UnsupportedOperationException("Show me how to handle " + T.getName());
            }
            setters.add(setter);
        }
    }

    /**
     * Fill a component instance with the relevant values from a configuration.
     *
     * @param component Component instance.
     * @param config JSON element associated with this component.
     */
    public void loadFor(Component component, JsonObject config) {
        try {
            for (Setter setter : setters)
                setter.set(component, config);

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields in component", e);
        }
    }

    @FunctionalInterface
    private interface Setter {
        void set(Component instance, JsonObject config) throws IllegalAccessException;
    }

    /**
     * Marks a component field. When an entity is created from some configuration
     * the Property fields in components will be initialized with the values stored
     * in the config.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Property { }
}
