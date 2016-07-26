package deimos.basecomponents;

import deimos.Component;
import deimos.ConfigLoader;

/**
 * Basic mesh loader for the default renderer.
 */
public class StaticMeshComponent extends Component {

    @ConfigLoader.Property
    private String fileName;
}
