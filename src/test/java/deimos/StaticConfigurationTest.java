package deimos;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deimos.component.TestComponentWithVectors;
import deimos.component.TestInitComponentHook;
import deimos.component.TestPositionComponent;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.joml.Vector4i;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class StaticConfigurationTest {

    public static Entity findEntityById(Collection<Entity> entities, String id) {
        for (Entity entity : entities) {
            if (entity.getId().equals(id))
                return entity;
        }
        Assert.fail("No entity by ID " + id);
        return null;
    }

    public static Scene initGameAndConfiguredScene(String path) {
        Game game = new Game();

        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        Scene scene = game.addScene("scene", new InputStreamReader(is));

        Engine.test(game);
        return scene;
    }

    public static JsonObject createJsonObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    @Test
    public void testSimpleEntities() {
        Scene scene = initGameAndConfiguredScene("configurations/simple_entities.json");

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

        Assert.assertTrue(big.isRootNode());
        Assert.assertFalse(small.isRootNode());

        // Check values
        TestPositionComponent bigPos = big.getComponent(TestPositionComponent.class);
        bigPos.assertPosition(999, 1000);

        TestPositionComponent smallPos = small.getComponent(TestPositionComponent.class);
        smallPos.assertPosition(11, 22);
    }

    @Test
    public void testPostConfigHook() {
        Scene scene = initGameAndConfiguredScene("configurations/extra_config.json");
        List<Entity> entities = scene.getEntitiesWith(TestInitComponentHook.class);
        Assert.assertEquals(1, entities.size());

        TestInitComponentHook component = entities.get(0).getComponent(TestInitComponentHook.class);
        Assert.assertEquals(11, component.getX());
        Assert.assertEquals(11 + 22, component.getCombined());
    }

    @Test
    public void testVectorTypes() {
        Scene scene = initGameAndConfiguredScene("configurations/entity_with_vectors.json");
        List<Entity> entities = scene.getEntitiesWith(TestComponentWithVectors.class);
        Assert.assertEquals(1, entities.size());

        TestComponentWithVectors component = entities.get(0).getComponent(TestComponentWithVectors.class);

        Assert.assertEquals(new Vector2i(1, 2), component.vec2i);
        Assert.assertEquals(new Vector3i(1, 2, 3), component.vec3i);
        Assert.assertEquals(new Vector4i(1, 2, 3, 4), component.vec4i);
    }

    @Test
    public void testBooleanGetterEmpty() {
        JsonObject o = createJsonObject("{ }");
        Assert.assertFalse(ConfigLoader.getBoolean(o, "bool", false));
        Assert.assertTrue(ConfigLoader.getBoolean(o, "bool", true));
    }

    @Test
    public void testBooleanGetter() {
        JsonObject o = createJsonObject("{ \"bool\": true }");
        Assert.assertTrue(ConfigLoader.getBoolean(o, "bool", false));
    }

    @Test
    public void testListGetterEmpty() {
        JsonObject o = createJsonObject("{ }");
        Assert.assertEquals(Collections.emptyList(), ConfigLoader.getList(o, "list", String.class));
    }

    @Test
    public void testListGetterStrings() {
        JsonObject o = createJsonObject("{ \"list\": [ \"yo\", \"hello\" ] }");
        Assert.assertEquals(
                Arrays.asList("yo", "hello"),
                ConfigLoader.getList(o, "list", String.class));
    }

    @Test
    public void testListGetterInts() {
        JsonObject o = createJsonObject("{ \"list\": [ -1, 0, 123 ] }");
        Assert.assertEquals(
                Arrays.asList(-1, 0, 123),
                ConfigLoader.getList(o, "list", int.class));
    }


}
