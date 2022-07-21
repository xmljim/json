package io.github.xmljim.json.model;

import java.util.Collection;

/**
 * An enumeration of node/value types
 */
public enum NodeType {
    /**
     * A JsonArray
     */
    ARRAY(false, false),
    /**
     * A JsonObject
     */
    OBJECT(false, false),
    /**
     * A boolean value
     */
    BOOLEAN(false, true),
    /**
     * A double value
     */
    DOUBLE(true, true),
    /**
     * A float value
     *
     * @deprecated may remove in favor of using double
     */
    FLOAT(true, true),
    /**
     * An integer value
     */
    INTEGER(true, true),
    /**
     * A long value
     */
    LONG(true, true),
    /**
     * A numeric value ("parent" of all numeric value types)
     */
    NUMBER(true, true),
    /**
     * A null value
     */
    NULL(false, true),
    /**
     * A string value
     */
    STRING(false, true),
    /**
     * INTERNAL: Undefined value
     */
    UNDEFINED(false, false);

    private final boolean numeric;
    private final boolean primitive;

    NodeType(boolean numeric, boolean primitive) {
        this.numeric = numeric;
        this.primitive = primitive;
    }

    /**
     * returns true if the node type is a number type
     *
     * @return true if the node type is a number type
     */
    public boolean isNumeric() {
        return this.numeric;
    }

    /**
     * Returns true if the value is a primitive value type (String, Number, Boolean)
     *
     * @return true if the value is a primitive value type
     */
    public boolean isPrimitive() {
        return this.primitive;
    }

    /**
     * Returns true if the value is a JsonArray
     *
     * @return true if the value is a JsonArray
     */
    public boolean isArray() {
        return this.name().equals(ARRAY.name());
    }

    /**
     * Returns true if the value is a JsonObject
     *
     * @return true if the value is a JsonObject
     */
    public boolean isObject() {
        return this.name().equals(OBJECT.name());
    }

    /**
     * Do not use
     *
     * @return do not use
     */
    public boolean isUndefined() {
        return this.name().equals(UNDEFINED.name());
    }

    public static NodeType fromClassType(Class<?> classType) {
        if (classType.equals(String.class)) {
            return STRING;
        }

        if (classType.equals(int.class) || classType.equals(Integer.class)) {
            return INTEGER;
        }

        if (classType.equals(long.class) || classType.equals(Long.class)) {
            return LONG;
        }

        if (classType.equals(double.class) || classType.equals(Double.class)) {
            return DOUBLE;
        }

        if (classType.equals(boolean.class) || classType.equals(Boolean.class)) {
            return BOOLEAN;
        }

        if (Collection.class.isAssignableFrom(classType)) {
            return ARRAY;
        }

        if (classType.isArray()) {
            return ARRAY;
        }

        if (!classType.isPrimitive()) {
            return OBJECT;
        }

        return UNDEFINED;
    }
}
