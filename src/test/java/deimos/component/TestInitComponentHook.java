package deimos.component;

import com.google.gson.JsonObject;
import deimos.Component;
import deimos.ConfigLoader;
import deimos.listener.PostConfigHook;
import org.junit.Assert;

public class TestInitComponentHook extends Component implements PostConfigHook {
    private int combined = -1;

    @ConfigLoader.Property
    private int x;

    @Override
    public void finalizeConfiguration(JsonObject config) {
        Assert.assertTrue(config.has("post"));

        combined = x + config.get("post").getAsInt();
    }

    public int getCombined() {
        return combined;
    }

    public int getX() {
        return x;
    }
}
