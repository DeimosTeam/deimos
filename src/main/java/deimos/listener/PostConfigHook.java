package deimos.listener;

import com.google.gson.JsonObject;

/**
 * Used to tag components which needs to run some code after being
 * loaded from a configuration file.
 */
public interface PostConfigHook {

    /**
     * Called after all {@link deimos.ConfigLoader.Property} fields in the
     * component instance have been initialized.
     *
     * @param config The JSON object associated with this component instance.
     */
    void finalizeConfiguration(JsonObject config);
}
