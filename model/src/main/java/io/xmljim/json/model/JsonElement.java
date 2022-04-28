package io.xmljim.json.model;

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
        }
        return (JsonObject) this;
    }


}
