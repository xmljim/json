package io.github.xmljim.json.jsonpath.context;

import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.jsonpath.JsonPathException;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.JsonNode;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Context {

    private final JsonElement current;
    private final Context root;
    private Context parent;
    private final Accessor accessor;
    private final String path;
    private final Map<String, Context> variables = new HashMap<>();

    protected Context(JsonNode root) {
        this.current = root;
        this.accessor = Accessor.root();
        this.path = accessor.toString();
        this.root = this;
    }

    protected Context(JsonElement element, Context parentContext, Accessor accessor) {
        this.current = element;
        this.parent = parentContext;
        this.accessor = accessor;
        this.root = parentContext.root;
        this.path = parentContext.path + "[" + accessor.toString() + "]";
    }

    protected <T> Context(T value) {
        ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
        this.current = elementFactory.newValue(value);
        this.parent = null;
        this.accessor = null;
        this.root = null;
        this.path = null;
    }

    public static Context create(JsonNode jsonNode) {
        if (jsonNode.type().isArray()) {
            return new ArrayContext(jsonNode.asJsonArray());
        } else {
            return new ObjectContext(jsonNode.asJsonObject());
        }
    }


    public static Context create(JsonElement element, Context parentContext, Accessor accessor) {
        return switch (element.type()) {
            case ARRAY -> new ArrayContext(element.asJsonArray(), parentContext, accessor);
            case OBJECT -> new ObjectContext(element.asJsonObject(), parentContext, accessor);
            default -> new ValueContext(element.asJsonValue(), parentContext, accessor);
        };
    }

    public static <T extends Temporal> Context createTemporalContext(T value) {
        if (value instanceof LocalDate localDate) {
            return new DateContext(localDate);
        } else if (value instanceof LocalDateTime localDateTime) {
            return new DateTimeContext(localDateTime);
        } else {
            throw new JsonPathException("Invalid Temporal Value");
        }
    }

    public static <T> Context createSimpleContext(T value) {
        return new SimpleValueContext(value);
    }

    public static Context defaultContext() {
        return new SimpleValueContext(null);
    }

    public JsonElement get() {
        return current;
    }

    public <T> T value() {
        return get().asJsonValue().value();
    }

    public JsonArray array() {
        return get().asJsonArray();
    }

    public JsonObject object() {
        return get().asJsonObject();
    }

    public Context root() {
        return root;
    }

    public String path() {
        return path;
    }

    public abstract boolean canTraverse();

    public abstract Stream<Context> stream();

    public abstract int childLength();

    public DataType type() {
        return DataType.fromNodeType(current.type());
    }

    public void setVariable(String key, Context context) {
        root.variables.put(key, context);
    }

    public Context getVariable(String key) {
        return root.variables.get(key);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Context o) {
            return Objects.equals(get(), o.get());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    public Optional<Context> getParent() {
        return Optional.ofNullable(parent);
    }

    public String toString() {
        return get().toJsonString();
    }
}
