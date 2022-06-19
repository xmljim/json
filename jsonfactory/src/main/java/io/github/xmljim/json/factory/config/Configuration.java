package io.github.xmljim.json.factory.config;

import java.util.Optional;
import java.util.stream.Stream;

public interface Configuration {

    <V> boolean put(ConfigKey configKey, V value);

    default <K, V> boolean put(K key, V value) {
        return put(ConfigKey.of(key), value);
    }

    <V> boolean putIfAbsent(ConfigKey configKey, V value);

    default <K, V> boolean putIfAbsent(K key, V value) {
        return putIfAbsent(ConfigKey.of(key), value);
    }

    <V> Optional<V> getOptional(ConfigKey configKey);

    default <K, V> Optional<V> getOptional(K key) {
        return getOptional(ConfigKey.of(key));
    }

    @SuppressWarnings("unchecked")
    default <V> V getOrDefault(ConfigKey configKey, V defaultIfMissing) {
        return (V) getOptional(configKey).orElse(defaultIfMissing);
    }

    default <K, V> V getOrDefault(K key, V defaultIfMissing) {


        return getOrDefault(ConfigKey.of(key), defaultIfMissing);
    }

    default <V> V get(ConfigKey configKey) {
        return getOrDefault(configKey, null);
    }

    default <K, V> V get(K key) {
        return get(ConfigKey.of(key));
    }

    void setImmutable(boolean immutable);

    boolean isImmutable();

    boolean containsKey(ConfigKey configKey);

    default <K> boolean containsKey(K key) {
        return containsKey(ConfigKey.of(key));
    }

    <V> boolean containsValue(V value);

    default Stream<ConfigKey> configKeys() {
        return entries().map(ConfigurationEntry::getKey);
    }

    default <V> Stream<V> keys() {
        return configKeys().map(ConfigKey::get);
    }

    default <V> Stream<V> values() {
        return entries().map(ConfigurationEntry::getValue);
    }

    Stream<ConfigurationEntry> entries();


}
