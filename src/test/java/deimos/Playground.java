package deimos;

import deimos.component.*;

public class Playground {

    public static void main(String[] args) {

        // GAME
        Game game = new Game();

        game.addComponent(GameComp1.class);
        game.addComponent(GameComp2.class);

        // SCENES
        Scene start = game.addScene("start");
        Scene space = game.addScene("space");
        Scene planet = game.addScene("planet");

        start.addComponent(SceneComp1.class);
        start.addComponent(SceneComp2.class);

        // ENTITIES
        Entity player = start.addEntity();
        Entity enemy = start.addEntity();

        player.addComponent(EntityComp1.class);
        player.addComponent(EntityComp2.class);


        // ENGINE
        Engine engine = Engine.test(game);
        engine.tick();
    }

}
