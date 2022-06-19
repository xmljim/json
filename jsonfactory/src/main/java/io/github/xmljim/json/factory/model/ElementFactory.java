package io.github.xmljim.json.factory.model;

import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.service.JsonService;

/**
 * Factory/service for creating Json elements
 */
public interface ElementFactory extends JsonService {

    /**
     * Create a new JsonValue
     *
     * @param value the raw value
     * @param <V>   the JsonValue type
     * @param <T>   the raw value type
     * @return a new JsonValue
     */
    <V, T> JsonValue<V> newValue(T value);

    /**
     * create a new JsonValue
     *
     * @param value  the raw value
     * @param parent the parent element
     * @param <V>    the JsonValue type
     * @param <T>    the raw value type
     * @return a new JsonValue
     * @deprecated Do not use, will be removed at a later date
     */
    @Deprecated
    <V, T> JsonValue<V> newValue(T value, JsonElement parent);

    /**
     * Create a new JsonObject
     *
     * @return a new JsonObject instance
     */
    JsonObject newObject();

    /**
     * Create a new JsonObject
     *
     * @param parent the parent element
     * @return a new JsonObject instance
     * @deprecated do not use, will be removed
     */
    @Deprecated
    JsonObject newObject(JsonElement parent);

    /**
     * Create a new JsonArray
     *
     * @return a new JsonArray
     */
    JsonArray newArray();

    /**
     * Create a new JsonArray
     *
     * @param parent the parent element
     * @return a new JsonArray
     * @deprecated do not use, will be removed
     */
    @Deprecated
    JsonArray newArray(JsonElement parent);
}
