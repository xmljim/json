package io.xmljim.json.factory.model;

import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;
import io.xmljim.json.service.JsonService;

public interface ElementFactory extends JsonService {

    <V, T> JsonValue<V> newValue(T value);

    <V, T> JsonValue<V> newValue(T value, JsonElement parent);

    JsonObject newObject();

    JsonObject newObject(JsonElement parent);

    JsonArray newArray();

    JsonArray newArray(JsonElement parent);
}
