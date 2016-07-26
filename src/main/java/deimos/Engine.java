package deimos;

import deimos.listener.OnInit;
import deimos.listener.OnStart;
import deimos.listener.OnStop;
import deimos.listener.OnTick;
import deimos.renderer.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Engine {
    private static final Logger log = LoggerFactory.getLogger(Entity.class);

    // Singleton
    static Engine o;

    // Testing Mode
    private static boolean testMode;

    private final Game game;
    private boolean running;
    private List<OnTick> tickListeners;
    private List<Component> newComponents;

    public static void start(Game game) {
        if (o != null)
            throw new IllegalStateException("Engine can only be started once.");
        newEngine(game);
    }

    public static void test(Game game) {
        testMode = true;
        newEngine(game);
    }

    private static void newEngine(Game game) {
        o = new Engine(game);
        o.run();
    }

    private Engine(Game game) {
        this.game = game;
    }

    private void run() {
        running = true;
        try {
            init();
            if (testMode) return;
            loop();
        } catch (Exception e) {
            log.error("Uncaught exception in main loop", e);
        } finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        newComponents = new ArrayList<>();
        tickListeners = new ArrayList<>();
        game.load();
    }

    private void loop() {
        while (running) {
            tick();
            render();
        }
    }

    private void render() {
        Renderer renderer = Objects.requireNonNull(game.getRenderer(), "No renderer attached.");

        for (Entity root : game.getCurrentScene().getRootEntities()) {
            renderer.startRendering();
            renderVisit(renderer, root);
            renderer.endRendering();
        }
    }

    private void renderVisit(Renderer renderer, Entity node) {
        boolean advance = true;

        if (renderer.preRenderFilter(node))
            advance = renderer.renderVisitNode(node);

        if (advance) {
            for (Entity child : node)
                renderVisit(renderer, child);
        }
    }

    private void cleanup() {
        // Haha, no.
    }

    /**
     * Get a handle to the current engine.
     *
     * @return instance.
     */
    public static Engine get() {
        return o;
    }

    void tick() {
        if (!o.newComponents.isEmpty()) {
            List<Component> temp = new ArrayList<>(o.newComponents);
            o.newComponents.clear();

            for (Component component : temp) {
                if (component instanceof OnStart)
                    ((OnStart) component).onStart();

                if (component instanceof OnTick)
                    o.tickListeners.add((OnTick) component);
            }
        }

        new ArrayList<>(o.tickListeners).forEach(OnTick::onTick);
    }

    static void initComponent(Component component) {
        if (component instanceof OnInit)
            ((OnInit) component).onInit();
        o.newComponents.add(component);
    }

    static void stopComponent(Component component) {
        if (component instanceof OnTick)
            o.tickListeners.remove(component);
        if (component instanceof OnStop)
            ((OnStop) component).onStop();
    }
}
