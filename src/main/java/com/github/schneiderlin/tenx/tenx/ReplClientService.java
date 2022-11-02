package com.github.schneiderlin.tenx.tenx;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(name = "tenx", storages = @Storage("tenx.xml"))
public class ReplClientService implements PersistentStateComponent<ReplClientService.State> {
    private final ReplClient client = new ReplClient();

    private State state;

    public static class State {
        public Map<String, String> cache;
    }

    public void start() {
        client.start(7888);
    }

    public void setCache(String key, String value) {
        state.cache.put(key, value);
    }

    public String getCache(String key) {
        return state.cache.get(key);
    }

    @Override
    public @Nullable State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public void noStateLoaded() {
        State state1 = new State();
        state1.cache = new HashMap<>();
        state = state1;
    }
}
