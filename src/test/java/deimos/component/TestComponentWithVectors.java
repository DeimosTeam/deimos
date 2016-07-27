package deimos.component;

import deimos.Component;
import deimos.ConfigLoader;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.joml.Vector4i;

public class TestComponentWithVectors extends Component {

    @ConfigLoader.Property
    public Vector2i vec2i;

    @ConfigLoader.Property
    public Vector3i vec3i;

    @ConfigLoader.Property
    public Vector4i vec4i;
}
