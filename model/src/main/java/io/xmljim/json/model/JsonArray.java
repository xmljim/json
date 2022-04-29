package io.xmljim.json.model;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public non-sealed interface JsonArray extends JsonNode {

    default NodeType type() {
        return NodeType.ARRAY;
    }

    <V> boolean add(V value);

    boolean addAll(Collection<?> collection);

    boolean addAll(Stream<?> stream);

    <V> boolean insert(int index, V value);

    boolean insertAll(int index, Collection<?> collection);

    boolean insertAll(int index, Stream<?> stream);

    <V> JsonValue<V> set(int index, V value);

    <V> Optional<JsonValue<V>> value(int index);

    @SuppressWarnings("unchecked")
    default <V> Optional<V> getOptional(int index) {
        return (Optional<V>) value(index).map(JsonValue::get);
    }

    @SuppressWarnings("unchecked")
    default <V> JsonValue<V> getValue(int index) {
        return (JsonValue<V>) value(index).orElse(null);
    }

    @SuppressWarnings("unchecked")
    default <V> V get(int index) {
        return (V) getOptional(index).orElse(null);
    }

    <V> boolean contains(V value);

    boolean containsValue(JsonValue<?> value);

    <V> JsonValue<V> remove(int index);

    Stream<JsonValue<?>> splice(int start);

    Stream<JsonValue<?>> splice(int start, int length);

    Stream<JsonValue<?>> jsonValues();

    Stream<?> values();

}
