package io.xmljim.json.elementfactory;

import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.NodeType;

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
