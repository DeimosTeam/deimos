package deimos;

import deimos.basecomponents.MaterialComponent;
import deimos.renderer.SimpleForwardRenderer;

import java.io.InputStream;
import java.io.InputStreamReader;

public class RendererExample {

    public static void main(String[] args) {
        Game game = new Game()
                .setRenderer(new SimpleForwardRenderer());

        Scene scene = game.addScene("main_scene");
        Entity root = scene.addEntity("root_entity");

        root.addComponent(MaterialComponent.class);


        Engine.start(game);
    }
}
