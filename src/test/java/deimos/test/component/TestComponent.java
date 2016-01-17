package deimos.test.component;

import deimos.Component;
import deimos.listener.OnInit;
import deimos.listener.OnStart;
import deimos.listener.OnStop;
import deimos.listener.OnTick;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestComponent extends Component implements OnInit, OnStart, OnTick, OnStop {

    private Map<String, Integer> map = new HashMap<>();

    public void increment(String key) {
        if (map.containsKey(key))
            map.put(key, map.get(key) + 1);
        else
            map.put(key, 1);
    }

    public int get(String key) {
        if (map.containsKey(key))
            return map.get(key);
        else
            return 0;
    }

    public void assertNumCalls(int inits, int starts, int ticks, int stops) {
        assertEquals("Testing number of inits:", inits, get("init"));
        assertEquals("Testing number of starts:", starts, get("start"));
        assertEquals("Testing number of ticks:", ticks, get("tick"));
        assertEquals("Testing number of stops:", stops, get("stop"));
    }

    @Override
    public void onInit() {
        increment("init");
    }

    @Override
    public void onStart() {
        increment("start");
    }

    @Override
    public void onTick() {
        increment("tick");
    }

    @Override
    public void onStop() {
        increment("stop");
    }
}
