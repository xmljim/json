package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.JsonNode;

public abstract class ExpressionFactory {
    public static final String PATH_PATTERN = "[$|@]([\\[|.].*]?)*";
    public static final String REGEX_PATTERN = "/(?<pattern>.*?)/(?<flags>[ixmusd]*)";
    public static final String STRING_PATTERN = "'.*?'";
    public static final String NULL_PATTERN = "null";
    public static final String BOOLEAN_PATTERN = "true|false";
    public static final String NUMBER_PATTERN = "[-+]?[0-9]*\\.?[0-9]*";
    public static final String LIST_PATTERN = "\\[.*(,\\s?.*)+]";
    public static final String VARIABLE_PATTERN = "\\{(?<key>[A-Za-z][A-Za-z0-9-]*)(?<path>#.*)?}";

    public static final String FUNCTION_PATTERN = "(?<function>[a-z0-9\\-]+)\\((?<args>.*)\\)";

    public static Expression create(String expression, Global global) {
        if (expression.matches(PATH_PATTERN)) {
            return new PathExpression(expression, global);
        }

        if (expression.matches(REGEX_PATTERN)) {
            return new RegexExpression(expression, global);
        }

        if (expression.matches(STRING_PATTERN)) {
            return new StringExpression(expression, global);
        }

        if (expression.matches(NULL_PATTERN)) {
            return new NullExpression(expression, global);
        }

        if (expression.matches(BOOLEAN_PATTERN)) {
            return new BooleanExpression(expression, global);
        }

        if (expression.matches(NUMBER_PATTERN)) {
            return new NumericExpression(expression, global);
        }

        if (expression.matches(LIST_PATTERN)) {
            return new ListExpression(expression, global);
        }

        if (expression.matches(VARIABLE_PATTERN)) {
            return new VariableExpression(expression, global);
        }

        if (expression.matches(FUNCTION_PATTERN)) {
            return new FunctionExpression(expression, global);
        }


        return null;
    }

    /**
     * Creates a predicate expression from a {@link JsonNode}
     *
     * <p>
     * The typical use for this argType of {@link Expression} is
     * for global variable objects expressed as a predicate expression.
     * For example, {@code {document('path/to/document')}}
     * </p>
     *
     * @param node the JsonNode
     * @return a {@link Expression}
     */
    public static Expression withJsonNode(JsonNode node, Global global) {
        return new JsonNodeExpression(node, global);
    }

    public static Expression withJsonNode(JsonNode node, Global global, String path) {
        return new JsonNodeExpression(node, global, path);
    }

}
