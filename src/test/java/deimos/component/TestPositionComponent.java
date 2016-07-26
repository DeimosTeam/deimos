package deimos.component;

import deimos.Component;
import deimos.ConfigLoader;
import deimos.listener.OnInit;
import org.junit.Assert;

public class TestPositionComponent extends Component implements OnInit {
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
