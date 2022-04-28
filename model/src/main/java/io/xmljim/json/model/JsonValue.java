package io.xmljim.json.model;

public non-sealed interface JsonValue<T> extends JsonElement, Comparable<JsonValue<T>> {

    T get();
}
