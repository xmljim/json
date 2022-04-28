package io.xmljim.json.factory.mapper;

import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Mapper {
    MapperConfig getConfig();

    JsonObject toJson(Map<String, ?> map);

    JsonObject toJson(Object o);

    JsonArray toJson(Collection<?> collection);

    Map<String, ?> toMap(JsonObject jsonObject);

    List<?> toList(JsonArray jsonArray);

    default JsonValue<?> toValue(Object value) {
        return toValue(value, null);
    }

    JsonValue<?> toValue(Object value, JsonElement parent);

    <T> T toClass(JsonObject jsonObject);

    <T> T toClass(JsonObject jsonObject, Class<T> targetClass);
}
