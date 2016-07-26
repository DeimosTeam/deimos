package deimos;

import deimos.component.TestPositionComponent;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StaticConfigurationTest {

    public static Entity findEntityById(Collection<Entity> entities, String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id))
                return entity;
        }
        Assert.fail("No entity by ID " + id);
        return null;
    }

    @Test
    public void testSimpleEntities() throws FileNotFoundException {
        Game game = new Game();

        InputStream is = ClassLoader.getSystemResourceAsStream("configurations/simple_entities.json");
        Scene scene = game.addScene("scene", new InputStreamReader(is));

        Engine.test(game);

        List<Entity> entities = scene.getEntitiesWith(TestPositionComponent.class);

        Assert.assertEquals(2, entities.size());

        Entity big = findEntityById(entities, "big_entity");
        Entity small = findEntityById(entities, "small_entity");

        // Check children
        List<Entity> bigChildren = new ArrayList<>();
        for (Entity child : big)
            bigChildren.add(child);

        Assert.assertEquals(1, bigChildren.size());
        Assert.assertTrue(bigChildren.contains(small));

        // Check values
        TestPositionComponent bigPos = big.getComponent(TestPositionComponent.class);
        bigPos.assertPosition(999, 1000);

        TestPositionComponent smallPos = small.getComponent(TestPositionComponent.class);
        smallPos.assertPosition(11, 22);
    }
}
