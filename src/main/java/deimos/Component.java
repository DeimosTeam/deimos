package deimos;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {
    private static final Map<Class<? extends Component>, ConfigLoader> loaders = new HashMap<>();

    private Game game;
    private Scene scene;
    private Entity entity;

    public static Component instantiate(Class<? extends Component> clazz, Game game, Scene scene, Entity entity) throws Exception {
        Component component = clazz.newInstance();
        component.game = game;
        component.scene = scene;
        component.entity = entity;

        if (entity != null) {
            JsonObject config = entity.getInitialConfigFor(clazz);
            if (config != null) {
                if (!loaders.containsKey(clazz))
                    loaders.put(clazz, new ConfigLoader(clazz));

                loaders.get(clazz).loadFor(component, config);
            }
        }
        return component;
    }

    @NotNull
    public Game game() {
        return game;
    }

    @Nullable
    public Scene scene() {
        return scene;
    }

    @Nullable
    public Entity entity() {
        return entity;
    }

}
