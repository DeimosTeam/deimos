package deimos.renderer;

import deimos.Entity;

public class SimpleForwardRenderer extends Renderer {
    @Override
    public boolean renderVisitNode(Entity node) {
        return false;
    }
}
