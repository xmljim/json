package io.github.xmljim.json.jsonpath.util;

import io.github.xmljim.json.model.NodeType;

public enum DataType {
    CONTEXT,
    REGEX,
    STRING,
    INTEGER,
    LONG,
    DOUBLE,
    BOOLEAN,
    NULL,
    FUNCTION,
    LIST,
    NODE,
    VARIABLE,
    DATE,
    DATETIME,
    OBJECT,
    ARRAY,
    UNDEFINED,
    /**
     * Special argType used for function and argument definitions
     */
    ANY;


    public boolean isPrimitive() {
        return this == STRING || this == INTEGER || this == LONG || this == DOUBLE || this == BOOLEAN;
    }

    public boolean isNumeric() {
        return this == INTEGER || this == LONG || this == DOUBLE;
    }

    public boolean isObject() {
        return this == OBJECT;
    }

    public boolean isArray() {
        return this == ARRAY;
    }

    public boolean isString() {
        return this == STRING;
    }

    public boolean isNull() {
        return this == NULL;
    }

    public boolean isTemporal() {
        return this == DATE || this == DATETIME;
    }

    public static DataType fromNodeType(NodeType nodeType) {
        return valueOf(nodeType.name());
    }


}
