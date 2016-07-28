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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Helper class used for loading JSON configuration into a component instance.
 */
public class ConfigLoader {
    private static final Map<Class<? extends Component>, ConfigLoader> loaders = new HashMap<>();
    private static final Map<Class<?>, Function<JsonElement, Object>> types = new HashMap<>();

    static {
        // Base types
        types.put(     String.class, JsonElement::getAsString);

        types.put(        int.class, JsonElement::getAsInt);
        types.put(    Integer.class, JsonElement::getAsInt);

        // JSON types
        types.put( JsonObject.class, JsonElement::getAsJsonObject);

        // Vector types
        types.put(   Vector2i.class, j -> new Vector2i(toIntBuffer(j.getAsJsonArray())));
        types.put(   Vector3i.class, j -> new Vector3i(toIntBuffer(j.getAsJsonArray())));
        types.put(   Vector4i.class, j -> new Vector4i(toIntBuffer(j.getAsJsonArray())));
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
        IntBuffer buffer = IntBuffer.allocate(array.size());
        array.forEach(elm -> buffer.put(elm.getAsInt()));
        buffer.rewind();
        return buffer;
    }

    private static Function<JsonElement, Object> getterFor(Class<?> type) {
        Function<JsonElement, Object> getter = types.get(type);

        return Objects.requireNonNull(getter, () -> "Show me how to handle " + type);
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
            Function<JsonElement, Object> valueGetter = getterFor(type);

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
     * Try to get a boolean setting from a JSON object.
     *
     * @param object The object.
     * @param name Name of the setting.
     * @param def Default value if the name is not found.
     * @return value.
     */
    public static boolean getBoolean(JsonObject object, String name, boolean def) {
        if (!object.has(name))
            return def;
        return object.get(name).getAsBoolean();
    }

    /**
     * Get a list setting from a JSON object and convert each element to a specific type.
     *
     * @param object The object.
     * @param name Name of the setting.
     * @param type Class of the converted type.
     * @return the resulting list, empty list if the name does not exist in the object.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(JsonObject object, String name, Class<T> type) {
        if (!object.has(name))
            return Collections.emptyList();

        Function<JsonElement, Object> getter = getterFor(type);

        JsonArray array = object.get(name).getAsJsonArray();
        List<T> lst = new ArrayList<>(array.size());

        for (JsonElement element : array)
            lst.add((T)getter.apply(element));
        return lst;
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
