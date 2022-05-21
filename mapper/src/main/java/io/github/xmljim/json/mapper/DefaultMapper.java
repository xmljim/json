package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.Mapper;
import io.github.xmljim.json.factory.mapper.MapperConfig;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.mapper.exception.JsonMapperException;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.service.ServiceManager;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DefaultMapper implements Mapper {
    private final MapperConfig mapperConfig;
    private final ElementFactory elementFactory;
    private final MapperFactory factory;

    public DefaultMapper(MapperConfig mapperConfig, MapperFactory factory) {
        this.mapperConfig = mapperConfig;
        this.factory = factory;
        if (ServiceManager.isServiceAvailable(ElementFactory.class)) {
            this.elementFactory = ServiceManager.getProvider(ElementFactory.class);
        } else {
            throw new JsonMapperException(ElementFactory.class.getSimpleName() + " service provider is not available");
        }
    }

    @Override
    public MapperConfig getConfig() {
        return mapperConfig;
    }

    @Override
    public JsonObject toJson(Map<String, ?> map) {
        JsonObject jsonObject = elementFactory.newObject();
        map.forEach((key, value) -> {
            if (!mapperConfig.getIgnoreKeys().contains(key)) {
                jsonObject.put(key, toValue(value, jsonObject));
            }
        });

        return jsonObject;
    }

    @Override
    public JsonObject toJson(Object o) {
        return ClassScanner.toJsonObject(o, elementFactory, this, factory);
    }

    @Override
    public JsonArray toJson(Collection<?> collection) {
        JsonArray jsonArray = elementFactory.newArray();
        collection.forEach(item -> jsonArray.add(toValue(item, jsonArray)));
        return jsonArray;
    }

    @Override
    public Map<String, Object> toMap(JsonObject jsonObject) {
        Map<String, Object> newMap = new LinkedHashMap<>();

        jsonObject.keys().forEach(key -> {
            if (!getConfig().getIgnoreKeys().contains(key)) {
                newMap.put(key, convertJsonValue(jsonObject.value(key).orElse(elementFactory.newValue(null))));
            }
        });

        return newMap;
    }

    @Override
    public List<Object> toList(JsonArray jsonArray) {
        return jsonArray.jsonValues().map(this::convertJsonValue).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonValue<?> toValue(Object value, JsonElement parent) {
        if (value == null) {
            return elementFactory.newValue(null, parent);
        } else if (value instanceof String v) {
            return elementFactory.newValue(v, parent);
        } else if (value instanceof Boolean v) {
            return elementFactory.newValue(v, parent);
        } else if (value instanceof Number v) {
            return elementFactory.newValue(v, parent);
        } else if (value instanceof Map v) {
            return elementFactory.newValue(toJson(v), parent);
        } else if (value instanceof Collection v) {
            return elementFactory.newValue(toJson(v), parent);
        } else {
            return elementFactory.newValue(toJson(value), parent);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T toClass(JsonObject jsonObject) {
        return (T) toClass(jsonObject, getConfig().getTargetClass().orElseThrow(() -> new JsonMapperException("No Target Class defined")));
    }

    @Override
    public <T> T toClass(JsonObject jsonObject, Class<T> targetClass) {
        return ClassScanner.toClass(targetClass, jsonObject, elementFactory, this, factory);
    }

    private <V> Object convertJsonValue(JsonValue<V> jsonValue) {
        if (jsonValue.type().isPrimitive()) {
            return jsonValue.get();
        } else if (jsonValue.type().isArray()) {
            return toList((JsonArray) jsonValue.get());
        } else {
            return toMap((JsonObject) jsonValue.get());
        }
    }
}
