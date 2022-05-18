package io.xmljim.json.model;

import io.xmljim.json.exception.JsonException;

public sealed interface JsonElement permits JsonNode, JsonValue {

    NodeType type();

    JsonElement parent();

    String toJsonString();

    String prettyPrint();

    String prettyPrint(int indent);

    default boolean isNode() {
        return !type().isPrimitive();
    }

    default boolean isValue() {
        return type().isPrimitive();
    }

    boolean isEquivalent(JsonElement other);

    default JsonValue<?> asJsonValue() {
        return (JsonValue<?>) this;
    }

    default JsonArray asJsonArray() {
        if (this instanceof JsonValue<?> arrayValue) {
            return (JsonArray) arrayValue.get();
        }

        return (JsonArray) this;
    }

    default JsonObject asJsonObject() {
        if (this instanceof JsonValue<?> arrayValue) {
            return (JsonObject) arrayValue.get();
        } else if (this instanceof JsonObject jsonObject) {
            return jsonObject;
        }
        return null;
    }

    default String asString() {
        if (this instanceof JsonValue<?> value && this.type() == NodeType.STRING) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }

    default Number asNumber() {
        if (this instanceof JsonValue<?> value && this.type().isNumeric()) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }

    default Boolean asBoolean() {
        if (this instanceof JsonValue<?> value && this.type() == NodeType.BOOLEAN) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }


}
