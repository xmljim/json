package io.github.xmljim.json.factory.model;

import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.service.JsonService;

public interface ElementFactory extends JsonService {

    <V, T> JsonValue<V> newValue(T value);

    <V, T> JsonValue<V> newValue(T value, JsonElement parent);

    JsonObject newObject();

    JsonObject newObject(JsonElement parent);

    JsonArray newArray();

    JsonArray newArray(JsonElement parent);
}
