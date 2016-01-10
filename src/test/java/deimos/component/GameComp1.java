package deimos.component;

import deimos.*;
import deimos.listener.OnAwake;
import deimos.listener.OnStart;
import deimos.listener.OnTick;

import java.util.Arrays;
import java.util.List;

public class GameComp1 extends Component implements OnAwake, OnStart, OnTick {

    private String name = "GameComp1  ";

    @Override
    public void onAwake() {
        System.out.println(name + " is waking");
    }

    @Override
    public void onStart() {
        System.out.println(name + " is starting");
    }

    @Override
    public void onTick() {
        System.out.println(name + " is ticking");
    }

    public void printName() {
        System.out.println(name);
    }

}
