package deimos.component;

import deimos.Component;
import deimos.ConfigLoader;
import org.junit.Assert;

public class TestPositionComponent extends Component {
    private boolean initialized;

    @ConfigLoader.Property
    private int x;

    @ConfigLoader.Property
    private Integer y;

    @Override
    public void onInit() {
        initialized = true;
    }

    public void assertPosition(int x, Integer y) {
        Assert.assertTrue(initialized);
        Assert.assertEquals(this.x, x);
        Assert.assertEquals(this.y, y);
    }
}
