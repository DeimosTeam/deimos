package deimos.component;

import deimos.Component;
import deimos.listener.OnAwake;
import deimos.listener.OnStart;
import deimos.listener.OnTick;

public class GameComp2 extends Component implements OnAwake, OnStart, OnTick {

    private String name = "GameComp2  ";

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
