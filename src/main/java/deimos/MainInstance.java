package deimos;

import deimos.listener.OnTick;

/**
 * Used to make component where one instance has a unique role.
 *
 * @see deimos.basecomponents.Camera
 */
public interface MainInstance {

    /**
     * The normal {@link OnTick#onTick()} event will only be invoked for the main
     * component instance. This method is used by the engine to query which one,
     * if several exist. In case more than one {@link MainInstance} component
     * returned true, there is no guarantee which one will be chosen.
     *
     * @return Whether or not this instance is the main one.
     */
    boolean isMainInstance();
}
