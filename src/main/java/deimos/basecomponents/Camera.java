package deimos.basecomponents;

import deimos.Component;
import deimos.MainInstance;

public class Camera extends Component implements MainInstance {

    @Override
    public boolean isMainInstance() {
        return false;
    }
}
