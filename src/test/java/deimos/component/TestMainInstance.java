package deimos.component;

import deimos.Component;
import deimos.MainInstance;
import deimos.listener.OnTick;

public class TestMainInstance extends Component implements OnTick, MainInstance {

    private boolean main = false;
    private int ticks = 0;

    public void setMain(boolean main) {
        this.main = main;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public boolean isMainInstance() {
        return main;
    }

    @Override
    public void onTick() {
        ticks++;
    }
}
