package io.xmljim.json.model;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public non-sealed interface JsonObject extends JsonNode {

    boolean containsKey(String key);

    <V, T> JsonValue<V> put(String key, T value);

    <V, T> JsonValue<V> putIfAbsent(String key, T value);

    void putAll(Map<String, ?> map);

    <V> JsonValue<V> remove(String key);

    @SuppressWarnings("unchecked")
    default <V> V get(String key) {
        return (V) getOptional(key).orElse(null);
    }

    @SuppressWarnings("unchecked")
    default <V> Optional<V> getOptional(String key) {
        return (Optional<V>) value(key).map(JsonValue::get);
    }

    @SuppressWarnings("unchecked")
    default <V> V getOrDefault(String key, V defaultValue) {
        return (V) getOptional(key).orElse(defaultValue);
    }

    <V> Optional<JsonValue<V>> value(String key);

    @SuppressWarnings("unchecked")
    default <V> JsonValue<V> getValue(String key) {
        return (JsonValue<V>) value(key).orElse(null);
    }

    Stream<String> keys();

    Stream<?> values();
}
