package io.github.xmljim.json.mapper;

import io.github.xmljim.json.factory.mapper.*;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.mapper.exception.JsonMapperException;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.service.ServiceManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MapperImpl implements Mapper {
    private final Mapping mapping;
    private final MapperFactory mapperFactory;

    private final ElementFactory elementFactory;

    public MapperImpl(MapperFactory mapperFactory, Mapping mapping) {
        this.mapperFactory = mapperFactory;
        this.mapping = mapping;
        if (ServiceManager.isServiceAvailable(ElementFactory.class)) {
            this.elementFactory = ServiceManager.getProvider(ElementFactory.class);
        } else {
            throw new JsonMapperException(ElementFactory.class.getSimpleName() + " service provider is not available");
        }
    }

    @Override
    public MapperConfig getConfig() {
        return null;
    }

    @Override
    public Mapping getMapping() {
        return mapping;
    }

    @Override
    public JsonObject toJson(Map<String, ?> map) {
        JsonObject jsonObject = elementFactory.newObject();
        map.forEach((key, value) -> {
            jsonObject.put(key, toValue(value, jsonObject));
        });

        return jsonObject;
    }

    @Override
    public JsonObject toJson(Object o) {
        ClassMapping classMapping = mapping.getClassMapping(o.getClass());
        JsonObject jsonObject = elementFactory.newObject();
        classMapping.getMemberMappings().forEach(memberMapping -> memberMapping.applyToJson(o, jsonObject));
        return jsonObject;
    }

    @Override
    public JsonArray toJson(Collection<?> collection) {
        JsonArray jsonArray = elementFactory.newArray();
        collection.forEach(item -> jsonArray.add(toValue(item, jsonArray)));
        return jsonArray;
    }

    @Override
    public Map<String, Object> toMap(JsonObject jsonObject) {
        Map<String, Object> newMap = new HashMap<>();

        jsonObject.keys().forEach(key -> {
            if (!getConfig().getIgnoreKeys().contains(key)) {
                newMap.put(key, convertJsonValue(jsonObject.value(key).orElse(elementFactory.newValue(null))));
            }
        });

        return newMap;
    }

    @Override
    public List<?> toList(JsonArray jsonArray) {
        return jsonArray.jsonValues().map(this::convertJsonValue).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonValue<?> toValue(Object value, JsonElement parent) {
        if (value == null) {
            return elementFactory.newValue(null);
        } else if (value instanceof String v) {
            return elementFactory.newValue(v);
        } else if (value instanceof Boolean v) {
            return elementFactory.newValue(v);
        } else if (value instanceof Number v) {
            return elementFactory.newValue(v);
        } else if (value instanceof Map v) {
            return elementFactory.newValue(toJson(v));
        } else if (value instanceof Collection v) {
            return elementFactory.newValue(toJson(v));
        } else {
            return elementFactory.newValue(toJson(value));
        }
    }

    @Override
    public <T> T toClass(JsonObject jsonObject) {
        ClassMapping classMapping = mapping.getClassMappings().findFirst().orElseThrow(() -> new JsonMapperException("No Target Class Defined"));
        return convertToClass(jsonObject, classMapping);
    }

    @Override
    public <T> T toClass(JsonObject jsonObject, Class<T> targetClass) {
        ClassMapping classMapping = mapping.getClassMapping(targetClass);
        return convertToClass(jsonObject, classMapping);
    }

    private <T> T convertToClass(JsonObject jsonObject, ClassMapping classMapping) {
        return classMapping.toClass(jsonObject);
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
