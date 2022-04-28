package io.xmljim.json.jsonpath.predicate.expression;

public enum ExpressionType {
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
    VARIABLE;

    public boolean isPrimitive() {
        return this == STRING || this == INTEGER || this == LONG || this == DOUBLE || this == BOOLEAN;
    }

    public boolean isNumeric() {
        return this == INTEGER || this == LONG || this == DOUBLE;
    }
}
