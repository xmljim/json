package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JsonObjectImpl extends AbstractJsonNode implements JsonObject {
    private final Map<String, JsonValue<?>> data = new LinkedHashMap<>();

    public JsonObjectImpl(ElementFactory elementFactory) {
        super(elementFactory, NodeType.OBJECT, null);
    }

    public JsonObjectImpl(ElementFactory elementFactory, JsonElement parent) {
        super(elementFactory, NodeType.OBJECT, parent);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V, T> JsonValue<V> put(String key, T value) {
        return (JsonValue<V>) data.put(key, getElementFactory().newValue(value, this));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V, T> JsonValue<V> putIfAbsent(String key, T value) {
        return (JsonValue<V>) data.putIfAbsent(key, getElementFactory().newValue(value, this));
    }

    @Override
    public void putAll(Map<String, ?> map) {
        map.forEach((key, value) -> put(key, getElementFactory().newValue(value, this)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> JsonValue<V> remove(String key) {
        return (JsonValue<V>) data.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Optional<JsonValue<V>> value(String key) {
        JsonValue<V> value = (JsonValue<V>) data.getOrDefault(key, null);
        return Optional.ofNullable(value);
    }


    @Override
    public Stream<String> keys() {
        return data.keySet().stream();
    }

    @Override
    public Stream<?> values() {
        return data.values().stream().map(JsonValue::get);
    }

    @Override
    public int compareTo(JsonNode other) {

        if (other instanceof JsonObject compare) {
            AtomicInteger comp = new AtomicInteger();
            //key size must be equivalent
            if (this.size() == compare.size()) {
                //if key size is equivalent, then compare key values
                keys().forEach(key -> {
                    if (getValue(key).compareTo(compare.getValue(key)) != 0) {
                        //increment the comparator value if values are not equal
                        comp.getAndIncrement();
                    }
                });
            } else {
                comp.set(this.size() - compare.size());
            }

            return comp.get();
        }

        return -1;
    }

    @Override
    public boolean isEquivalent(JsonElement other) {
        if (other instanceof JsonObject object) {
            return compareTo(object) == 0;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonObjectImpl that)) return false;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, parent());
    }

    @Override
    public String toJsonString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");

        Set<String> entries =
            data.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\"" +
                    ":" +
                    e.getValue().toJsonString())
                .collect(Collectors.toSet());
        builder.append(String.join(",", entries));
        builder.append("}");

        return builder.toString();
    }

    @Override
    public String prettyPrint() {
        return prettyPrint(0);
    }

    @Override
    public String prettyPrint(int indent) {
        final StringBuffer startStopIndent = new StringBuffer();
        startStopIndent.append(" ".repeat(Math.max(0, indent * 4)));
        final StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("\n");

        final String indentString = "    ";

        Set<String> entries = data.entrySet().stream()
            .map(e -> {
                StringBuilder entryBuffer = new StringBuilder();
                final String key = e.getKey();
                final String value = e.getValue().prettyPrint(indent + 1);

                entryBuffer.append(startStopIndent)
                    .append(indentString).append("\"").append(key).append("\"")
                    .append(": ")
                    .append(value);
                return entryBuffer.toString();
            })
            .collect(Collectors.toSet());

        builder.append(String.join(",\n", entries));
        builder.append("\n");
        builder.append(startStopIndent);
        builder.append("}");

        return builder.toString();
    }

    public String toString() {
        return toJsonString();
    }
}
