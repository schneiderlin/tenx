package com.example.linzihao97.plugindemo.tenx;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ReplClientService implements PersistentStateComponent<Map<String, String>> {
    private final ReplClient client = new ReplClient();
    private Map<String, String> cache;

    public void start() {
        client.start(7888);
    }

    public void setCache(String key, String value) {
        cache.put(key, value);
    }

    public String getCache(String key) {
        return cache.get(key);
    }

    @Override
    public @Nullable Map<String, String> getState() {
        return cache;
    }

    @Override
    public void loadState(@NotNull Map<String, String> state) {
        this.cache = state;
    }

    @Override
    public void noStateLoaded() {
        cache = Map.of(
                "key1", "value1",
                "key2", "value2"
        );
//        PersistentStateComponent.super.noStateLoaded();
    }
}
