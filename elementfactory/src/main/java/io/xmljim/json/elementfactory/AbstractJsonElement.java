package io.xmljim.json.elementfactory;

import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.NodeType;

abstract class AbstractJsonElement {
    private final NodeType type;
    private final JsonElement parent;

    public AbstractJsonElement(NodeType type, JsonElement parent) {
        this.type = type;
        this.parent = parent;
    }

    public NodeType type() {
        return type;
    }

    public JsonElement parent() {
        return parent;
    }

}
