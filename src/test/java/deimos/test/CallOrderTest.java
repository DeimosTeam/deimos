package deimos.test;

import deimos.Engine;
import deimos.Entity;
import deimos.Game;
import deimos.Scene;
import deimos.test.component.TestComponent;
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
        scene2.addComponent(TestComponent.class);

        // Entities
        Entity entity1 = scene1.addEntity();
        entity1.addComponent(TestComponent.class);
        Entity entity2 = scene2.addEntity();
        entity2.addComponent(TestComponent.class);

        // Start engine engine and fetch instantiated components
        Engine engine = Engine.test(game);
        TestComponent gameComp = game.getComponent(TestComponent.class);
        TestComponent sceneComp1 = scene1.getComponent(TestComponent.class);
        TestComponent sceneComp2 = scene2.getComponent(TestComponent.class);
        TestComponent entityComp1 = entity1.getComponent(TestComponent.class);
        TestComponent entityComp2 = entity2.getComponent(TestComponent.class);

        // Only some should be initialized
        assertNotNull(gameComp);
        assertNotNull(sceneComp1);
        assertNull(sceneComp2);
        assertNotNull(entityComp1);
        assertNull(entityComp2);

        // Assert
        gameComp.assertNumCalls(1,0,0,0);
        sceneComp1.assertNumCalls(1,0,0,0);
        entityComp1.assertNumCalls(1,0,0,0);

        engine.tick();
        gameComp.assertNumCalls(1,1,1,0);
        sceneComp1.assertNumCalls(1,1,1,0);
        entityComp1.assertNumCalls(1,1,1,0);

        engine.tick();
        gameComp.assertNumCalls(1,1,2,0);
        sceneComp1.assertNumCalls(1,1,2,0);
        entityComp1.assertNumCalls(1,1,2,0);

        game.switchScene("scene2");
        gameComp.assertNumCalls(1,1,2,0);
        sceneComp1.assertNumCalls(1,1,2,1);
        entityComp1.assertNumCalls(1,1,2,1);

        gameComp = game.getComponent(TestComponent.class);
        sceneComp1 = scene1.getComponent(TestComponent.class);
        sceneComp2 = scene2.getComponent(TestComponent.class);
        entityComp1 = entity1.getComponent(TestComponent.class);
        entityComp2 = entity2.getComponent(TestComponent.class);

        // Now others shouldnt be initialized
        assertNotNull(gameComp);
        assertNull(sceneComp1);
        assertNotNull(sceneComp2);
        assertNull(entityComp1);
        assertNotNull(entityComp2);

        gameComp.assertNumCalls(1,1,2,0);
        sceneComp2.assertNumCalls(1,0,0,0);
        entityComp2.assertNumCalls(1,0,0,0);

        engine.tick();
        gameComp.assertNumCalls(1,1,3,0);
        sceneComp2.assertNumCalls(1,1,1,0);
        entityComp2.assertNumCalls(1,1,1,0);

        engine.tick();
        gameComp.assertNumCalls(1,1,4,0);
        sceneComp2.assertNumCalls(1,1,2,0);
        entityComp2.assertNumCalls(1,1,2,0);
    }




}
