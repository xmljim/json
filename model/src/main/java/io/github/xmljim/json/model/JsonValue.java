package io.github.xmljim.json.model;

/**
 * Represents a value contained by a JsonNode container instance
 *
 * @param <T> The value type
 */
public non-sealed interface JsonValue<T> extends JsonElement, Comparable<JsonValue<T>> {

    /**
     * Return the raw value
     *
     * @return the raw value
     */
    T get();

    /**
     * Return the casted value
     *
     * @param <V> the value type
     * @return the casted value
     */
    @SuppressWarnings("unchecked")
    default <V> V value() {
        return (V) get();
    }
}
