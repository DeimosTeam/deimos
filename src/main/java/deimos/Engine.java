package deimos;

import deimos.listener.OnAwake;
import deimos.listener.OnStart;
import deimos.listener.OnTick;

import java.util.ArrayList;
import java.util.List;

public class Engine {

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

    public static Engine test(Game game) {
        testMode = true;
        newEngine(game);
        return o;
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
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        newComponents = new ArrayList<>();
        tickListeners = new ArrayList<>();
        game.load();
        initNewComponents();
    }

    private void loop() {
        while (running) {
            tick();
        }
    }

    public void tick() {
        new ArrayList<>(tickListeners).forEach(OnTick::onTick);
    }

    private void cleanup() {

    }

    public static void initNewComponents() {
        if (o.newComponents.isEmpty()) return;

        List<Component> newComps = new ArrayList<>(o.newComponents);
        o.newComponents.clear();

        newComps.stream()
                .filter(comp -> comp instanceof OnAwake)
                .forEach(comp -> ((OnAwake) comp).onAwake());

        newComps.stream()
                .filter(comp -> comp instanceof OnStart)
                .forEach(comp -> ((OnStart) comp).onStart());

        newComps.stream()
                .filter(comp -> comp instanceof OnTick)
                .forEach(comp -> o.tickListeners.add((OnTick) comp));
    }

    public static void newComponent(Component component) {
        o.newComponents.add(component);
    }
}
