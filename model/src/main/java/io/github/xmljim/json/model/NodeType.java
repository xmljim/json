package io.github.xmljim.json.model;

public enum NodeType {
    ARRAY(false, false),
    OBJECT(false, false),
    BOOLEAN(false, true),
    DOUBLE(true, true),
    FLOAT(true, true),
    INTEGER(true, true),
    LONG(true, true),
    NUMBER(true, true),
    NULL(false, true),
    STRING(false, true),
    UNDEFINED(false, false);

    private final boolean numeric;
    private final boolean primitive;

    NodeType(boolean numeric, boolean primitive) {
        this.numeric = numeric;
        this.primitive = primitive;
    }

    public boolean isNumeric() {
        return this.numeric;
    }

    public boolean isPrimitive() {
        return this.primitive;
    }

    public boolean isArray() {
        return this.name().equals(ARRAY.name());
    }

    public boolean isObject() {
        return this.name().equals(OBJECT.name());
    }

    public boolean isUndefined() {
        return this.name().equals(UNDEFINED.name());
    }
}
