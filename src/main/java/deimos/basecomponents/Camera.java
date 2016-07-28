package deimos.basecomponents;

import deimos.Component;
import deimos.ConfigLoader;
import deimos.Entity;
import deimos.renderer.Renderer;

import java.util.Objects;

public class Camera extends Component {
    private static Camera primaryCamera; // TODO hvordan sættes fra config? "@property is_primary".... og? Lav måske med custom og hav en CameraPrimaryFlag type?

    @ConfigLoader.Property
    private Renderer renderer;

    public static Camera getPrimaryCamera() {
        return primaryCamera;
    }

    public void setPrimary() {
        primaryCamera = this;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void renderFrame() {
        Objects.requireNonNull(renderer, "No renderer attached.");

        for (Entity root : game().getCurrentScene().getRootEntities()) {
            renderer.startRendering();
            renderVisit(root);
            renderer.endRendering();
        }
    }

    private void renderVisit(Entity node) {
        boolean advance = true;

        if (renderer.preRenderFilter(node))
            advance = renderer.renderVisitNode(node);

        if (advance) {
            for (Entity child : node)
                renderVisit(child);
        }
    }

    @Override
    public void onStop() {
        if (primaryCamera == this)
            primaryCamera = null;
    }
}
