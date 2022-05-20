package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JsonArrayImpl extends AbstractJsonNode implements JsonArray {
    private final List<JsonValue<?>> valueList = new ArrayList<>();

    public JsonArrayImpl(ElementFactory elementFactory) {
        super(elementFactory, NodeType.ARRAY, null);
    }

    public JsonArrayImpl(ElementFactory elementFactory, JsonElement parent) {
        super(elementFactory, NodeType.ARRAY, parent);
    }

    @Override
    public void clear() {
        valueList.clear();
    }

    @Override
    public int size() {
        return valueList.size();
    }

    @Override
    public <V> boolean add(V value) {
        return valueList.add(getElementFactory().newValue(value, this));
    }

    @Override
    public boolean addAll(Collection<?> collection) {
        return addAll(collection.stream());
    }

    @Override
    public boolean addAll(Stream<?> stream) {
        List<JsonValue<Object>> values = stream.map(item -> getElementFactory().newValue(item)).toList();
        return valueList.addAll(values);
    }

    @Override
    public <V> boolean insert(int index, V value) {
        valueList.add(index, getElementFactory().newValue(value));
        return true;
    }

    @Override
    public boolean insertAll(int index, Collection<?> collection) {
        return insertAll(index, collection.stream());
    }

    @Override
    public boolean insertAll(int index, Stream<?> stream) {
        List<JsonValue<Object>> values = stream.map(item -> getElementFactory().newValue(item)).toList();
        return valueList.addAll(index, values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> JsonValue<V> set(int index, V value) {
        JsonValue<V> val = getElementFactory().newValue(value);
        return (JsonValue<V>) valueList.set(index, val);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Optional<JsonValue<V>> value(int index) {
        JsonValue<V> value = null;
        try {
            value = (JsonValue<V>) valueList.get(index);
        } catch (Exception e) {
            //no-op
        }
        return Optional.ofNullable(value);
    }

    @Override
    public <V> boolean contains(V value) {
        return values().anyMatch(item -> item.equals(value));
    }

    @Override
    public boolean containsValue(JsonValue<?> value) {
        return valueList.contains(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> JsonValue<V> remove(int index) {
        return (JsonValue<V>) valueList.remove(index);
    }

    @Override
    public Stream<JsonValue<?>> splice(int start) {
        return null;
    }

    @Override
    public Stream<JsonValue<?>> splice(int start, int length) {
        return null;
    }

    @Override
    public Stream<JsonValue<?>> jsonValues() {
        return valueList.stream();
    }

    @Override
    public Stream<?> values() {
        return jsonValues().map(JsonValue::get);
    }

    @Override
    public int compareTo(JsonNode o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonArrayImpl jsonArray)) {
            return false;
        }

        System.out.printf("equals array: %1$d %2$d", valueList, jsonArray.valueList);
        System.out.println();
        return Objects.equals(valueList, jsonArray.valueList);
    }

    @Override
    public boolean isEquivalent(JsonElement other) {
        if (other instanceof JsonArray array) {
            return equals(array);
        }

        return false;
    }

    @Override
    public String toJsonString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");

        List<String> values = jsonValues().map(JsonValue::toJsonString).collect(Collectors.toList());

        builder.append(String.join(", ", values));
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String prettyPrint() {
        return prettyPrint(0);
    }

    @Override
    public String prettyPrint(int indent) {
        final StringBuilder startStopIndent = new StringBuilder();
        startStopIndent.append(" ".repeat(Math.max(0, indent * 4)));

        final StringBuilder builder = new StringBuilder();
        final String indentString = "    ";

        builder.append("[");
        builder.append("\n");

        List<String> values = jsonValues().map(jsonValue -> startStopIndent +
            indentString +
            jsonValue.prettyPrint(indent + 1)).collect(Collectors.toList());

        builder.append(String.join(",\n", values));

        builder.append("\n");
        builder.append(startStopIndent);
        builder.append("]");

        return builder.toString();
    }

    public String toString() {
        return toJsonString();
    }
}
