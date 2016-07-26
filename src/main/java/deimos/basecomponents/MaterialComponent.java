package deimos.basecomponents;

import deimos.Component;
import deimos.ConfigLoader;

/**
 * Basic material loader for the default renderer.
 */
public class MaterialComponent extends Component {

    @ConfigLoader.Property
    private String fileName;
}
