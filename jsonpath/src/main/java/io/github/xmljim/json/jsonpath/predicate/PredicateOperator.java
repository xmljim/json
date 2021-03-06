package io.github.xmljim.json.jsonpath.predicate;

import java.util.Arrays;

public enum PredicateOperator {
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL_THAN(">="),
    LESS_THAN("<"),
    LESS_OR_EQUAL_THAN("<="),
    REGEX_MATCH("=~"),
    IN("in"),
    NOT_IN("nin"),
    STARTS_WITH("startswith"),
    ENDS_WITH("endswith"),
    CONTAINS("contains"),
    SIZE("size"),
    EMPTY("empty"),
    IS_NOT_NULL("isnotnull");

    private final String operator;

    PredicateOperator(String operator) {
        this.operator = operator;
    }

    public static PredicateOperator find(String value) {
        return Arrays.stream(values()).filter(p -> p.operator.equalsIgnoreCase(value)).findFirst().orElse(null);
    }

    public String getOperator() {
        return operator;
    }
}
