package deimos;

public class Scene extends ComponentHolder {

    private final String id;
    private Game game;

    public Scene(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    void setGame(Game game) {
        this.game = game;
    }

    public void load() throws Exception {
        loadComponents(game, this, null);
    }
}
