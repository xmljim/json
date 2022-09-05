package io.github.xmljim.json.jsonpath.util;

import io.github.xmljim.json.jsonpath.predicate.PredicateOperator;

import java.util.Arrays;

final class PatternConstants {
    private PatternConstants() {
        //no-op
    }

    private static final String NAME = "\\w\\-";
    private static final String NAME_START = "[a-zA-Z_]";
    private static final String JSON_KEY = NAME_START + "[" + NAME + "]+";

    private static final String COMPARATORS = "!~<>=";
    private static final String OPERATORS = "%^/+*";
    private static final String SEPARATORS = ":,";
    private static final String ENCLOSURES = "\\[\\]\\(\\)\\{\\}'";
    private static final String PREDICATE = "\\?";
    private static final String STRING = null;

    private static final String DOT_FILTER_PATTERN = or("\\." + JSON_KEY, "[.]", "[*]");
    private static final String JSON_KEY_SELECTOR_DOT = "(\\." + JSON_KEY + ")";  // .foo
    private static final String JSON_KEY_SELECTOR_BRACKET = "(\\['" + JSON_KEY + "'])"; // ['foo']
    private static final String JSON_INDEX_SELECTOR = "(\\[-?\\d+])"; // [0] or [-1]
    private static final String WILDCARD_SELECTOR_DOT = "(\\.\\*)"; // .*
    private static final String WILDCARD_SELECTOR_BRACKET = "(\\[\\*])"; // [*]

    private static final String RECURSION_SELECTOR_DOT = "(\\.\\.)"; // ..
    private static final String RECURSION_SELECTOR_BRACKET = "(\\[" + RECURSION_SELECTOR_DOT + "])"; // [..]

    private static final String UNION_FILTER_SELECTOR_NUMBER = "(\\[\\d+[,\\s?\\d+]+])";
    private static final String UNION_FILTER_SELECTOR_KEY = "(\\['" + JSON_KEY + "'[',\\s?'" + JSON_KEY + "']+])";
    private static final String SLICE_FILTER_SELECTOR = "\\[(\\-?\\d+)?\\s?:\\s?(\\-?\\d+)?]";

    private static final String FUNCTION_NAME = JSON_KEY;
    private static final String BOOLEAN_PATTERN = "true|false";
    private static final String NULL_PATTERN = "null";
    private static final String NUMBER_PATTERN = "[-+]?[0-9]*\\.?[0-9]*";
    private static final String STRING_PATTERN = "'[\\s\\S]*'";

    private static final String PRIMITIVE_VALUE_PATTERNS = group(or(BOOLEAN_PATTERN, NULL_PATTERN, NUMBER_PATTERN, STRING_PATTERN));

    private static final String PREDICATE_NOT = "!";
    private static final String PREDICATE_OPERATORS = or(Arrays.stream(PredicateOperator.values())
        .map(PredicateOperator::getOperator)
        .map(PatternConstants::wrapClass)
        .toArray(String[]::new));

    private static final String PATH_START = "[$@]";

    public static final String CHILD_FILTER = or(JSON_KEY_SELECTOR_DOT, JSON_KEY_SELECTOR_BRACKET, JSON_INDEX_SELECTOR);
    public static final String WILDCARD_FILTER = group(or(WILDCARD_SELECTOR_BRACKET, WILDCARD_SELECTOR_DOT));
    public static final String RECURSION_FILTER = group(or(RECURSION_SELECTOR_DOT, RECURSION_SELECTOR_BRACKET));
    public static final String UNION_FILTER = group(or(UNION_FILTER_SELECTOR_KEY, UNION_FILTER_SELECTOR_NUMBER));
    public static final String SLICE_FILTER = group(SLICE_FILTER_SELECTOR);

    private static final String PREDICATE_START = "\\[\\?\\(";
    private static final String PREDICATE_END = ")]";
    public static final String PREDICATE_FILTER = group(PREDICATE_START + PATH_START + or(CHILD_FILTER, WILDCARD_FILTER,
        RECURSION_FILTER, UNION_FILTER, SLICE_FILTER) + PREDICATE_END);
    private static final String PATH_COMPONENTS = or(CHILD_FILTER, WILDCARD_FILTER, RECURSION_FILTER, UNION_FILTER, SLICE_FILTER,
        PREDICATE_FILTER);

    //[$@](\.[a-zA-Z0-9_\-]+|[\*]|[\.])*
    //(\[\?\()])*

    //[$@]((\.[a-zA-Z0-9_\-]+|[\*]|[\.])*|(\[[\?()a-zA-Z0-9_\-\s!=><:*+/%,']+])*)*
    public static final String PATH_PATTERN = PATH_START + groupWithCardinality("*", PATH_COMPONENTS);


    private static String group(String... items) {
        return "(" + String.join("", items) + ")";
    }

    private static String groupWithCardinality(String cardinality, String... items) {
        return group(items) + cardinality;
    }

    private static String or(String... patterns) {
        return String.join("|", patterns);
    }

    private static String wrapClass(String item) {
        return "[" + item + "]";
    }

}
