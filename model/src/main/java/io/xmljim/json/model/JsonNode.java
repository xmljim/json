package io.xmljim.json.model;

public sealed interface JsonNode extends JsonElement, Comparable<JsonNode> permits JsonObject, JsonArray {

    int size();

    void clear();

    default boolean isEmpty() {
        return size() == 0;
    }
}
