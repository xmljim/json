package io.xmljim.json.elementfactory;

import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.NodeType;

class ObjectValue extends AbstractJsonValue<JsonObject> {

    public ObjectValue(JsonElement parent, JsonObject value) {
        super(NodeType.OBJECT, parent, value);
    }

    public ObjectValue(JsonObject value) {
        this(null, value);
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

    @Override
    public String toString() {
        return toJsonString();
    }
}
