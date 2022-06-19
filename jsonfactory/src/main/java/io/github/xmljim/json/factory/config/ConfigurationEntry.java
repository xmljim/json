package io.github.xmljim.json.factory.config;

import java.util.Objects;

public final class ConfigurationEntry {
    private final ConfigKey key;
    private final Object value;

    private <K, V> ConfigurationEntry(K key, V value) {
        this.key = ConfigKey.of(key);
        this.value = value;
    }

    private <V> ConfigurationEntry(ConfigKey key, V value) {
        this.key = key;
        this.value = value;
    }
    

    public static <K, V> ConfigurationEntry of(K key, V value) {
        return new ConfigurationEntry(key, value);
    }

    public static <V> ConfigurationEntry of(ConfigKey key, V value) {
        return new ConfigurationEntry(key, value);
    }

    public ConfigKey getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationEntry that = (ConfigurationEntry) o;
        return key.equals(that.key) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "ConfigurationEntry {" +
            "key=" + key +
            ", value=" + value +
            '}';
    }
}
