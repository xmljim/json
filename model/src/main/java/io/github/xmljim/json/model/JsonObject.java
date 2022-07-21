package io.github.xmljim.json.model;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents an associative array of elements with key-value pairs
 */
public non-sealed interface JsonObject extends JsonNode {

    /**
     * Returns whether the associative array contains a specified key
     *
     * @param key the key to locate
     * @return true if the key is found
     */
    boolean containsKey(String key);

    /**
     * Add/update a specified value in the associative array
     *
     * @param key   the key, cannot be null
     * @param value the value. Will be converted to a {@link JsonValue}
     * @param <V>   The Json value type
     * @param <T>   the raw value type
     * @return the JsonValue of the specified value assigned to the key
     */
    <V, T> JsonValue<V> put(String key, T value);

    /**
     * Add a new key-value pair, if the key does not exist
     *
     * @param key   the key
     * @param value the value
     * @param <V>   the JsonValue type
     * @param <T>   the raw value type
     * @return a JsonValue of the specified raw value
     */
    <V, T> JsonValue<V> putIfAbsent(String key, T value);

    /**
     * Append key-value pairs from a map
     *
     * @param map the map instance
     */
    void putAll(Map<String, ?> map);

    /**
     * Remove a value by its key
     *
     * @param key the key
     * @param <V> the value type
     * @return the removed value
     */
    <V> JsonValue<V> remove(String key);

    /**
     * Return a raw value from the associative array from the specified key
     *
     * @param key the key
     * @param <V> the raw value type
     * @return the raw value
     */
    @SuppressWarnings("unchecked")
    default <V> V get(String key) {
        Object value = getOptional(key).orElse(null);
        return (V) value;
    }

    /**
     * Return the optional value for a given key
     *
     * @param key the key
     * @param <V> the value type
     * @return The Optional for the given key. May be empty.
     */
    @SuppressWarnings("unchecked")
    default <V> Optional<V> getOptional(String key) {
        Optional<JsonValue<Object>> valueOptional = value(key);
        return valueOptional.map(objectJsonValue -> (Optional<V>) Optional.of(objectJsonValue.get())).orElseGet(() -> Optional.ofNullable(null));

    }

    /**
     * Return the given value for a key, or a default value if the key does not exist
     *
     * @param key          the key
     * @param defaultValue the default value if the key doesn't exist
     * @param <V>          the value type
     * @return the retrieved or default value
     */
    @SuppressWarnings("unchecked")
    default <V> V getOrDefault(String key, V defaultValue) {
        return (V) getOptional(key).orElse(defaultValue);
    }

    /**
     * Return the optional JsonValue for a given key
     *
     * @param key the key
     * @param <V> the value type
     * @return the Optional of the JsonValue.  May be empty if the key does not exist
     */
    <V> Optional<JsonValue<V>> value(String key);

    /**
     * Return the JsonValue for a given key
     *
     * @param key the key
     * @param <V> the value type
     * @return the JsonValue or null if the key doesn't exist
     */
    @SuppressWarnings("unchecked")
    default <V> JsonValue<V> getValue(String key) {
        return (JsonValue<V>) value(key).orElse(null);
    }

    /**
     * Return a stream of the associative array's keys
     *
     * @return stream of keys
     */
    Stream<String> keys();

    /**
     * Return a stream of the associative array's values
     *
     * @return the stream of raw values
     */
    Stream<?> values();
}
