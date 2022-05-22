package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.model.NodeType;

import java.util.Objects;

abstract class AbstractJsonValue<T> extends AbstractJsonElement implements JsonValue<T> {

    private final T value;

    public AbstractJsonValue(NodeType type, JsonElement parent, T value) {
        super(type, parent);
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public int compareTo(JsonValue<T> o) {
        return this.hashCode() - (o == null ? -1 : o.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractJsonValue<?> that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEquivalent(JsonElement other) {
        if (other instanceof JsonValue<?> val) {
            return compareTo((JsonValue<T>) val) == 0;
        }

        return false;
    }
}
