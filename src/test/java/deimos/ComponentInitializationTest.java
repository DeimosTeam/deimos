package deimos;

import deimos.component.TestInitComponent;
import org.junit.Assert;
import org.junit.Test;

public class ComponentInitializationTest {

    @Test
    public void testOnInit() {
        Game game = new Game();
        game.addComponent(TestInitComponent.class);

        Engine.test(game);

        TestInitComponent comp = game.getComponent(TestInitComponent.class);
        Assert.assertEquals("onInit", comp.getText());
        Assert.assertTrue(comp.onInitWasCalled());
    }

    @Test
    public void testInitializer() {
        String text = "Hello";

        Game game = new Game();
        game.addComponent(TestInitComponent.class, c -> {
            c.setText(text);
        });

        Engine.test(game);

        TestInitComponent comp = game.getComponent(TestInitComponent.class);
        Assert.assertEquals(text, comp.getText());
        Assert.assertFalse(comp.onInitWasCalled());
    }
}
