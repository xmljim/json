package io.xmljim.json.jsonpath.context;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.filter.Accessor;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.model.NodeType;
import io.xmljim.json.service.ServiceManager;

import java.util.HashMap;
import java.util.Map;
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

    public static <T> Context createSimpleContext(T value) {
        return new SimpleValueContext(value);
    }

    public static Context defaultContext() {
        return new SimpleValueContext(null);
    }

    public JsonElement get() {
        return current;
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

    public NodeType type() {
        return current.type();
    }

    public void setVariable(String key, Context context) {
        root.variables.put(key, context);
    }

    public void getVariable(String key) {
        root.variables.getOrDefault(key, null);
    }

    public boolean equals(Context other) {
        return get().equals(other.get());
    }

    public String toString() {
        return get().toJsonString();
    }
}
