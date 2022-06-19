package io.github.xmljim.json.factory.config;

import java.util.Objects;

public final class ConfigKey {
    private Object key;

    private <T> ConfigKey(T key) {
        this.key = key;
    }

    public static <T> ConfigKey of(T key) {
        Objects.requireNonNull(key, "Key value cannot be null");
        return new ConfigKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigKey configKey = (ConfigKey) o;
        return key.equals(configKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
