package deimos;

import deimos.component.ExitOnEscape;
import deimos.component.TestComp;

public class Playground {

    public static void main(String[] args) {

        Game game = new Game();

        Scene menu = game.addScene("menu");
        Scene space = game.addScene("space");
        Scene planet = game.addScene("planet");

        game.addComponent(ExitOnEscape.class);
        game.addComponent(TestComp.class);

//        Engine.start(game);

        Engine engine = Engine.test(game);
        engine.tick();
//        Engine engine = Engine.test(game);
    }

}
