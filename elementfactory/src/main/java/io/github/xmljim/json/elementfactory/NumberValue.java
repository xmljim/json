package io.github.xmljim.json.elementfactory;

import io.github.xmljim.json.model.JsonElement;
import io.github.xmljim.json.model.NodeType;

import java.util.Objects;

class NumberValue extends AbstractJsonValue<Number> {

    public NumberValue(JsonElement parent, Number value) {
        super(getNumberType(value), parent, value);

    }

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumberValue that)) {
            return false;
        }
        return Objects.equals(get().doubleValue(), that.get().doubleValue());
    }
}
