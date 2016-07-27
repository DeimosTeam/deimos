package deimos;

import deimos.component.TestMainInstance;
import org.junit.Assert;
import org.junit.Test;

public class ComponentTest {
    @Test
    public void testMainInstance() {
        Game game = new Game();
        Scene scene = game.addScene("scene");

        Entity e1 = scene.addEntity("e1");
        e1.addComponent(TestMainInstance.class);

        Entity e2 = scene.addEntity("e2");
        e2.addComponent(TestMainInstance.class);

        Engine.test(game);
        TestMainInstance c1 = e1.getComponent(TestMainInstance.class);
        TestMainInstance c2 = e2.getComponent(TestMainInstance.class);
        Assert.assertEquals(0, c1.getTicks() + c2.getTicks());

        c1.setMain(true);
        Engine.tick();
        Assert.assertEquals(1, c1.getTicks());
        Assert.assertEquals(0, c2.getTicks());

        c1.setMain(false);
        c2.setMain(true);
        Engine.tick();
        Assert.assertEquals(1, c1.getTicks());
        Assert.assertEquals(1, c2.getTicks());

        c1.setMain(true);
        Engine.tick();
        Assert.assertEquals(3, c1.getTicks() + c2.getTicks());
    }
}
