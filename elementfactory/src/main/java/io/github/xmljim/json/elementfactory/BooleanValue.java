package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.NodeType;

class BooleanValue extends AbstractJsonValue<Boolean> {

    public BooleanValue(JsonElement parent, Boolean value) {
        super(NodeType.BOOLEAN, parent, value);
    }

    public BooleanValue(Boolean value) {
        this(null, value);
    }

    @Override
    public String toJsonString() {
        return get().toString();
    }

    @Override
    public String prettyPrint() {
        return toString();
    }

    @Override
    public String prettyPrint(int indent) {
        return toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
