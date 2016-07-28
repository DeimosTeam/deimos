package deimos;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.joml.Vector4i;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Helper class used for loading JSON configuration into a component instance.
 */
public class ConfigLoader {
    private static final Map<Class<? extends Component>, ConfigLoader> loaders = new HashMap<>();
    private static final Map<Class<?>, Function<JsonElement, Object>> types = new HashMap<>();

    static {
        // Number types
        types.put(      int.class, JsonElement::getAsInt);
        types.put(  Integer.class, JsonElement::getAsInt);

        // Vector types
        types.put( Vector2i.class, j -> new Vector2i(toIntBuffer(j.getAsJsonArray())));
        types.put( Vector3i.class, j -> new Vector3i(toIntBuffer(j.getAsJsonArray())));
        types.put( Vector4i.class, j -> new Vector4i(toIntBuffer(j.getAsJsonArray())));
    }

    private final List<Field> fields = new ArrayList<>();
    private final String clazzName;

    private ConfigLoader(Class<? extends Component> clazz) {
        clazzName = clazz.getName();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Property.class) != null) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
    }

    private static IntBuffer toIntBuffer(JsonArray array) {
        IntBuffer buffer = IntBuffer.allocate(array.size()); // TODO kan optimize ved at holde static buffer.
        array.forEach(elm -> buffer.put(elm.getAsInt()));
        buffer.rewind();
        return buffer;
    }

    /**
     * Creates a closure for loading the value in 'config' into a component instance of type T.
     *
     * @param clazz Component type.
     * @param config A JSON object that contains the matching values for the fields tagged
     * by {@link Property} in the component.
     * @return A closure that, when called, will set the relevant values from 'config' into
     * the component passed as argument.
     */
    public static <T extends Component> Consumer<T> createConfigLoader(Class<T> clazz, JsonObject config) {
        if (!loaders.containsKey(clazz))
            loaders.put(clazz, new ConfigLoader(clazz));

        return loaders.get(clazz).createLoaderFor(config);
    }

    private <T extends Component> Consumer<T> createLoaderFor(JsonObject config) {
        JsonObject componentConfig = config.get(clazzName).getAsJsonObject();
        List<Setter> setters = new ArrayList<>();

        for (Field field : fields) {
            Class<?> type = field.getType();

            Function<JsonElement, Object> valueGetter = types.get(type);
            if (valueGetter == null)
                throw new IllegalStateException("Show me how to handle " + type);

            Object value = valueGetter.apply(componentConfig.get(field.getName()));
            setters.add(ins -> field.set(ins, value));
        }
        return instance -> {
            try {
                for (Setter setter : setters)
                    setter.set(instance);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set fields in component", e);
            }
        };
    }

    @FunctionalInterface
    private interface Setter {
        void set(Component instance) throws IllegalAccessException;
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
