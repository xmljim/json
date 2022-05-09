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
    VARIABLE,
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
}
