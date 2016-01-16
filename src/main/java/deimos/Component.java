package deimos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component {

    private Game game;
    private Scene scene;
    private Entity entity;

    public static Component instantiate(Class<? extends Component> clazz, Game game, Scene scene, Entity entity) throws Exception {
        Component component = clazz.newInstance();
        component.game = game;
        component.scene = scene;
        component.entity = entity;
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
