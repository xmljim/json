package io.github.xmljim.json.model;

public non-sealed interface JsonValue<T> extends JsonElement, Comparable<JsonValue<T>> {

    T get();

    @SuppressWarnings("unchecked")
    default <V> V value() {
        return (V) get();
    }
}
