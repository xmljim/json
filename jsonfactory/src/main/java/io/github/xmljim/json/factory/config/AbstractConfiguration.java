package io.github.xmljim.json.factory.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractConfiguration implements Configuration {
    private final Map<ConfigKey, ConfigurationEntry> configurationMap = new HashMap<>();
    private boolean immutable = true;

    @Override
    public <V> boolean put(ConfigKey configKey, V value) {
        if (isImmutable()) {
            return putIfAbsent(configKey, value);
        } else {
            configurationMap.put(configKey, ConfigurationEntry.of(configKey, value));
            return true;
        }
    }

    @Override
    public <V> boolean putIfAbsent(ConfigKey configKey, V value) {
        @SuppressWarnings("unchecked")
        V val = (V) configurationMap.putIfAbsent(configKey, ConfigurationEntry.of(configKey, value));
        return val == null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Optional<V> getOptional(ConfigKey configKey) {
        ConfigurationEntry entry = configurationMap.getOrDefault(configKey, null);
        if (entry != null) {
            return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }

    @Override
    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public boolean isImmutable() {
        return immutable;
    }

    @Override
    public boolean containsKey(ConfigKey configKey) {
        return configurationMap.containsKey(configKey);
    }

    @Override
    public <V> boolean containsValue(V value) {
        return configurationMap.containsValue(value);
    }

    @Override
    public Stream<ConfigurationEntry> entries() {
        return configurationMap.values().stream();
    }
}
