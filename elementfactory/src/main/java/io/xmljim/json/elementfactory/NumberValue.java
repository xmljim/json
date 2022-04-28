package io.xmljim.json.elementfactory;

import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.NodeType;

class NumberValue extends AbstractJsonValue<Number> {

    public NumberValue(JsonElement parent, Number value) {
        super(getNumberType(value), parent, value);

    }

    public Number get() {
        return switch (type()) {
            case INTEGER -> super.get().intValue();
            case LONG -> super.get().longValue();
            case FLOAT -> super.get().floatValue();
            default -> super.get().doubleValue();
        };
    }

    private static NodeType getNumberType(Number value) {
        NodeType type = null;

        if (value instanceof Integer) {
            type = NodeType.INTEGER;
        } else if (value instanceof Long) {
            type = NodeType.LONG;
        } else if (value instanceof Float) {
            type = NodeType.FLOAT;
        } else if (value instanceof Double) {
            type = NodeType.DOUBLE;
        } else {
            type = NodeType.NUMBER;
        }

        return type;
    }

    @Override
    public String toJsonString() {
        return get().toString();
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
