package io.xmljim.json.elementfactory;

import io.xmljim.json.elementfactory.exception.JsonUnsupportedTypeException;
import io.xmljim.json.factory.mapper.MapperFactory;
import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;
import io.xmljim.json.service.JsonServiceProvider;
import io.xmljim.json.service.ServiceManager;

import java.util.Collection;
import java.util.Map;

@JsonServiceProvider(service = ElementFactory.class, isNative = true, version = "1.0.1")
public class ElementFactoryImpl implements ElementFactory {
    private final boolean mapperProviderAvailable;
    private MapperFactory mapperFactory;

    public ElementFactoryImpl() {
        mapperProviderAvailable = ServiceManager.isServiceAvailable(MapperFactory.class);
        if (mapperProviderAvailable) {
            mapperFactory = ServiceManager.getProvider(MapperFactory.class);
        }
    }

    @Override
    public <V, T> JsonValue<V> newValue(T value) {
        return newValue(value, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V, T> JsonValue<V> newValue(T value, JsonElement parent) {
        if (value == null) {
            return (JsonValue<V>) newNullValue(parent);
        } else if (value instanceof String v) {
            return (JsonValue<V>) newStringValue(v, parent);
        } else if (value instanceof Boolean v) {
            return (JsonValue<V>) newBooleanValue(v, parent);
        } else if (value instanceof Number v) {
            return (JsonValue<V>) newNumberValue(v, parent);
        } else if (value instanceof JsonValue<?> v) {
            return (JsonValue<V>) v;
        } else if (value instanceof JsonObject v) {
            return (JsonValue<V>) newJsonObjectValue(v, parent);
        } else if (value instanceof JsonArray v) {
            return (JsonValue<V>) newJsonArrayValue(v, parent);
        } else if (value instanceof Map v) {
            return mapToValue(v);
        } else if (value instanceof Collection v) {
            return mapToValue(v);
        } else {
            return mapToValue(value);
        }

    }

    @SuppressWarnings("unchecked")
    private <V, T> JsonValue<V> mapToValue(T value) {
        if (mapperProviderAvailable) {
            return (JsonValue<V>) mapperFactory.newBuilder().build().toValue(value);
        } else {
            throwUnsupportedError(value.getClass());
        }

        return null;
    }

    private void throwUnsupportedError(Class<?> valueClass) {
        throw new JsonUnsupportedTypeException("Unsupported type: " + valueClass.getSimpleName() +
            ". Requires " + MapperFactory.class.getSimpleName() + " service provider");
    }

    @Override
    public JsonObject newObject() {
        return new JsonObjectImpl(this);
    }

    @Override
    public JsonObject newObject(JsonElement parent) {
        return new JsonObjectImpl(this, parent);
    }

    @Override
    public JsonArray newArray() {
        return new JsonArrayImpl(this);
    }

    @Override
    public JsonArray newArray(JsonElement parent) {
        return new JsonArrayImpl(this, parent);
    }

    private ArrayValue newJsonArrayValue(JsonArray array, JsonElement parent) {
        return new ArrayValue(parent, array);
    }

    private ObjectValue newJsonObjectValue(JsonObject object, JsonElement parent) {
        return new ObjectValue(parent, object);
    }

    private NullValue newNullValue(JsonElement parent) {
        return new NullValue(parent);
    }

    private NumberValue newNumberValue(Number value, JsonElement parent) {
        return new NumberValue(parent, value);
    }

    private BooleanValue newBooleanValue(Boolean value, JsonElement parent) {
        return new BooleanValue(parent, value);
    }

    private StringValue newStringValue(String value, JsonElement parent) {
        return new StringValue(parent, value);
    }
}
