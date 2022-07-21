package io.github.xmljim.json.model;

/**
 * Parent interface for Json containers (JsonArray and JsonObject)
 */
public sealed interface JsonNode extends JsonElement, Comparable<JsonNode> permits JsonObject, JsonArray {

    /**
     * Return the number of elements within the container
     *
     * @return the number of elements in the container
     */
    int size();

    /**
     * Clear the elements in the container
     */
    void clear();

    /**
     * Returns whether the container is empty
     *
     * @return true if no elements are contained within
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
