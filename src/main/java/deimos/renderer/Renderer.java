package deimos.renderer;

import deimos.Entity;
import deimos.basecomponents.MaterialComponent;
import deimos.basecomponents.StaticMeshComponent;

public abstract class Renderer {

    public void startRendering() { }

    // default implementation of transformation etc.
    // Returns if the node should be passed on to the render function, NOT if the walk should stop here.
    public boolean preRenderFilter(Entity node) {
        // TODO apply transform.
        return node.hasComponent(StaticMeshComponent.class) && node.hasComponent(MaterialComponent.class);

    }

    // Only nodes with Material and Mesh are passed to this function.
    public abstract boolean renderVisitNode(Entity node);

    public void endRendering() { }

}
