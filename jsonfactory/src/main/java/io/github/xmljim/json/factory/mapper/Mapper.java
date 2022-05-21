package io.github.xmljim.json.factory.mapper;

import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Mapper interface to map JsonElement to a specified type
 */
public interface Mapper {

    /**
     * Return the configuration for this mapper
     *
     * @return the mapper configuration
     */
    MapperConfig getConfig();

    /**
     * Convert a Map to a JsonObject
     *
     * @param map the Map instance
     * @return a new JsonObject
     */
    JsonObject toJson(Map<String, ?> map);

    /**
     * Convert an object instance to a JsonObject
     *
     * @param o the object to convert
     * @return a JsonObject instance
     */
    JsonObject toJson(Object o);

    /**
     * Convert a collection to a JsonArray
     *
     * @param collection the collection
     * @return a new JsonArray
     */
    JsonArray toJson(Collection<?> collection);

    /**
     * Convert a JsonObject to a Map
     *
     * @param jsonObject the JsonObject
     * @return a new Map instance
     */
    Map<String, Object> toMap(JsonObject jsonObject);

    /**
     * Convert a JsonArray to a List
     *
     * @param jsonArray the JsonArray
     * @return a new List instance
     */
    List<?> toList(JsonArray jsonArray);

    /**
     * Convert an object to a JsonValue
     *
     * @param value the value to convert
     * @return a new JsonValue instance
     */
    default JsonValue<?> toValue(Object value) {
        return toValue(value, null);
    }

    /**
     * Convert an object to a JsonValue and specify a parent
     *
     * @param value  The value to convert
     * @param parent the Parent element
     * @return a new JsonValue instance
     */
    @Deprecated
    JsonValue<?> toValue(Object value, JsonElement parent);

    /**
     * Convert a JsonObject into a specified class instance
     *
     * @param jsonObject The JsonObject to convert
     * @param <T>        The class type
     * @return a new class instance containing the data from the converted JsonObject
     */
    <T> T toClass(JsonObject jsonObject);

    /**
     * Convert a JsonObject into a class instance specified by the targetClass
     *
     * @param jsonObject  the JsonObject to convert
     * @param targetClass the target class to be created
     * @param <T>         the class type
     * @return a new instance of the targetClass
     */
    <T> T toClass(JsonObject jsonObject, Class<T> targetClass);
}
