package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.NodeType;

class StringValue extends AbstractJsonValue<String> {
    public StringValue(JsonElement parent, String value) {
        super(NodeType.STRING, parent, value);
    }

    public StringValue(String value) {
        this(null, value);
    }

    @Override
    public String toJsonString() {
        return "\"" + get()
            .replaceAll("\"", "\\\\\"")
            + "\"";
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
