package deimos.component;

import deimos.*;
import deimos.listener.OnAwake;
import deimos.listener.OnStart;
import deimos.listener.OnTick;

import java.util.Arrays;
import java.util.List;

public class ExitOnEscape extends Component implements OnAwake, OnStart, OnTick {

//    @Override
//    public List<Class<? extends Component>> getDependency() {
//        return Arrays.asList(ExitOnEscape.class, TestComp.class);
//    }

    @Override
    public void onAwake() {
        System.out.println("ExitOnEscape is waking");
    }

    @Override
    public void onStart() {
        System.out.println("ExitOnEscape is starting");
    }

    @Override
    public void onTick() {
        System.out.println("ExitOnEscape is ticking");
    }

}
