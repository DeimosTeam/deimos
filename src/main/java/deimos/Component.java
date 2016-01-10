package deimos;

public abstract class Component {

    private Game game;
    private Scene scene;
    private Entity entity;

    public Game game() {
        return game;
    }

    public Scene scene() {
        return scene;
    }

    public Entity entity() {
        return entity;
    }

    public static Component instantiate(Class<? extends Component> clazz, Game game, Scene scene, Entity entity) throws Exception {
        Component component = clazz.newInstance();
        component.game = game;
        component.scene = scene;
        component.entity = entity;
        Engine.newComponent(component);
        return component;
    }



}