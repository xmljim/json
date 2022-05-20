package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.NodeType;

class NullValue extends AbstractJsonValue<NullObject> {

    public NullValue(JsonElement parent) {
        super(NodeType.NULL, parent, null);
    }

    public NullValue() {
        super(NodeType.NULL, null, null);
    }

    @Override
    public String toJsonString() {
        return "null";
    }

    @Override
    public String prettyPrint() {
        return toJsonString();
    }

    @Override
    public String prettyPrint(int indent) {
        return toJsonString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
