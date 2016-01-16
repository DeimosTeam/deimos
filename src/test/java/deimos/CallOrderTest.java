package deimos;

import deimos.component.TestComponent;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class CallOrderTest {

    @Test
    public void testGameCallOrder() {

        // Arrange
        Game game = new Game();
        game.addComponent(TestComponent.class);
        Engine test = Engine.test(game);
        TestComponent comp = game.getComponent(TestComponent.class);

        // Assert
        comp.assertNumCalls(1,0,0,0);

        test.tick();
        comp.assertNumCalls(1,1,1,0);

        test.tick();
        comp.assertNumCalls(1,1,2,0);

        test.tick();
        comp.assertNumCalls(1,1,3,0);
    }

    @Test
    public void testSceneCallOrder() {

        // Arrange
        Game game = new Game();
        Scene scene = game.addScene("scene");
        scene.addComponent(TestComponent.class);

        Engine test = Engine.test(game);
        TestComponent comp = scene.getComponent(TestComponent.class);

        // Assert
        comp.assertNumCalls(1,0,0,0);

        test.tick();
        comp.assertNumCalls(1,1,1,0);

        test.tick();
        comp.assertNumCalls(1,1,2,0);

        test.tick();
        comp.assertNumCalls(1,1,3,0);

        game.switchScene("scene");
        comp.assertNumCalls(1,1,3,1);
    }

    @Test
    public void testEntityCallOrder() {

        // Arrange
        Game game = new Game();
        Scene scene = game.addScene("scene");
        Entity entity = scene.addEntity();
        entity.addComponent(TestComponent.class);

        Engine test = Engine.test(game);
        TestComponent comp = entity.getComponent(TestComponent.class);

        // Assert
        comp.assertNumCalls(1,0,0,0);

        test.tick();
        comp.assertNumCalls(1,1,1,0);

        test.tick();
        comp.assertNumCalls(1,1,2,0);

        test.tick();
        comp.assertNumCalls(1,1,3,0);

        game.switchScene("scene");
        comp.assertNumCalls(1,1,3,1);
    }

    @Test
    public void smokeTest() {

        // Arrange

        // Game
        Game game = new Game();
        game.addComponent(TestComponent.class);

        // Scenes
        Scene scene1 = game.addScene("scene1");
        scene1.addComponent(TestComponent.class);
        Scene scene2 = game.addScene("scene2");
        scene1.addComponent(TestComponent.class);

        // Entities
        Entity entity1 = scene1.addEntity();
        entity1.addComponent(TestComponent.class);
        Entity entity2 = scene2.addEntity();
        entity2.addComponent(TestComponent.class);

        // Start test engine and fetch instantiated components
        Engine test = Engine.test(game);
        TestComponent gameComp = game.getComponent(TestComponent.class);
        TestComponent scenComp1 = scene1.getComponent(TestComponent.class);
        TestComponent scenComp2 = scene2.getComponent(TestComponent.class);
        TestComponent entityComp1 = entity1.getComponent(TestComponent.class);
        TestComponent entityComp2 = entity2.getComponent(TestComponent.class);

        // Only some should be initialized
        assertNotNull(gameComp);
        assertNotNull(scenComp1);
        assertNull(scenComp2);
        assertNotNull(entityComp1);
        assertNull(entityComp2);

        gameComp.assertNumCalls(1,0,0,0);
        scenComp1.assertNumCalls(1,0,0,0);
        entityComp1.assertNumCalls(1,0,0,0);

        //
    }




}
