package deimos.component;

import deimos.Component;
import deimos.Engine;
import deimos.Game;
import deimos.listener.OnAwake;
import deimos.listener.OnStart;
import deimos.listener.OnTick;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestComp extends Component implements OnAwake, OnStart, OnTick {

    int counter = 0;

    @Override
    public void onAwake() {
        assertEquals(0, counter++);
        System.out.println("TestComp     is waking");
    }

    @Override
    public void onStart() {
        assertEquals(1, counter++);
        System.out.println("TestComp     is starting");
    }

    @Override
    public void onTick() {
        System.out.println("TestComp     is ticking");
        counter++;
    }

    @Test
    public void testAwakeStartTickOrder() {
        Game game = new Game();
        game.addComponent(TestComp.class);
        Engine engine = Engine.test(game);
        TestComp testComp = game.getComponent(TestComp.class);

        assertEquals(1, testComp.counter);
        engine.tick();
        assertEquals(3, testComp.counter);
    }
}
