package io.github.xmljim.json.model;

import io.github.xmljim.json.exception.JsonException;

/**
 * Root interface for all Json types
 */
public sealed interface JsonElement permits JsonNode, JsonValue {

    /**
     * The node type for the element
     *
     * @return the node type for the element
     */
    NodeType type();

    /**
     * @return the parent node
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    JsonElement parent();

    /**
     * Return the JSON string representation for this element
     *
     * @return
     */
    String toJsonString();

    /**
     * Pretty print the JSON string with indentation
     *
     * @return the indented JSON string
     */
    String prettyPrint();

    /**
     * Pretty print the JSON string with the specified indentation
     *
     * @param indent the number of spaces to index
     * @return
     */
    String prettyPrint(int indent);

    /**
     * Evaluates whether the element is a JsonNode (i.e., a JsonObject or JsonArray)
     *
     * @return true if the element is a JsonNode
     */
    default boolean isNode() {
        return !type().isPrimitive();
    }

    /**
     * Evaluates whether the element is a JsonValue (leaf node)
     *
     * @return true if the element is a leaf node
     */
    default boolean isValue() {
        return type().isPrimitive();
    }

    /**
     * Evaluates whether another element is equivalent to the current element.
     * Equivalence means that JsonObject key-value pairs are the same, though they
     * may not be in the same order; likewise, JsonArray values must be in the same
     * order and each value must be equal
     *
     * @param other the other element
     * @return true if they are equivalent
     */
    boolean isEquivalent(JsonElement other);

    /**
     * Cast the element to a JsonValue
     *
     * @return the element as a JsonValue
     * @throws ClassCastException if the element is not a JsonValue
     */
    default JsonValue<?> asJsonValue() {
        return (JsonValue<?>) this;
    }

    /**
     * Cast the element as a JsonArray
     *
     * @return the element as a JsonArray
     * @throws ClassCastException if the element is not a JsonArray
     */
    default JsonArray asJsonArray() {
        if (this instanceof JsonValue<?> arrayValue) {
            return (JsonArray) arrayValue.get();
        }

        return (JsonArray) this;
    }

    /**
     * Cast the element as a JsonObject
     *
     * @return the element as a JsonObject
     * @throws ClassCastException if the element is not a JsonObject
     */
    default JsonObject asJsonObject() {
        if (this instanceof JsonValue<?> arrayValue) {
            return (JsonObject) arrayValue.get();
        } else if (this instanceof JsonObject jsonObject) {
            return jsonObject;
        }
        return null;
    }

    /**
     * Return the element as a String value. The element <strong>must</strong> be
     * a {@link JsonValue} with a {@link NodeType} equal to {@link NodeType#STRING}
     *
     * @return the string value
     * @throws JsonException if the element is not a string value
     */
    default String asString() {
        if (this instanceof JsonValue<?> value && this.type() == NodeType.STRING) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }

    /**
     * Return the element as a Number value. The element <strong>must</strong> be
     * a {@link JsonValue} with a {@link NodeType} equal to {@link NodeType#NUMBER}
     * (or return {@link NodeType#isNumeric()} equal to {@code true})
     *
     * @return the string value
     * @throws JsonException if the element is not a number value
     */
    default Number asNumber() {
        if (this instanceof JsonValue<?> value && this.type().isNumeric()) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }

    /**
     * Return the element as a Number value. The element <strong>must</strong> be
     * a {@link JsonValue} with a {@link NodeType} equal to {@link NodeType#BOOLEAN}
     *
     * @return the string value
     * @throws JsonException if the element is not a boolean value
     */
    default Boolean asBoolean() {
        if (this instanceof JsonValue<?> value && this.type() == NodeType.BOOLEAN) {
            return value.value();
        } else {
            throw new JsonException("Invalid Cast: Cannot cast " + this.type() + " to String");
        }
    }


}
