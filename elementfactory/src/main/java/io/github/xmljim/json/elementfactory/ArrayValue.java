package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.NodeType;

class ArrayValue extends AbstractJsonValue<JsonArray> {
    public ArrayValue(JsonArray value) {
        this(null, value);
    }

    public ArrayValue(JsonElement parent, JsonArray value) {
        super(NodeType.ARRAY, parent, value);
    }

    @Override
    public String toJsonString() {
        return get().toJsonString();
    }

    @Override
    public String prettyPrint() {
        return get().prettyPrint();
    }

    @Override
    public String prettyPrint(int indent) {
        return get().prettyPrint(indent);
    }

    public String toString() {
        return get().toJsonString();
    }
}
