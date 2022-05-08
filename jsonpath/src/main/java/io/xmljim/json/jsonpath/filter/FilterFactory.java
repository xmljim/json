package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.variables.Global;

public abstract class FilterFactory {
    private static final String INDEX_FILTER = "\\[-?\\d+]";
    private static final String KEY_FILTER = "(\\[')?[_A-Za-z][_A-Za-z0-9\\-]*('])?";
    private static final String CHILD_FILTER = INDEX_FILTER + "|" + KEY_FILTER; //"\\[?[-']?[_A-Za-z0-9][_\\-A-Za-z0-9]*'?]?";
    private static final String CURRENT_FILTER = "@";
    private static final String ROOT_FILTER = "\\$";
    private static final String WILDCARD_FILTER = "\\[?\\*]?";
    private static final String RECURSION_FILTER = "\\[?\\.\\.]?";
    private static final String PREDICATE_FILTER = "\\[\\?\\(.*\\)]";
    private static final String UNION_FILTER = "\\[\\d+[,\\s?\\d+]+]";
    private static final String SLICE_FILTER = "\\[(?<start>\\-?\\d+)?:(?<end>\\-?\\d+)?]";
    private static final String FUNCTION_FILTER = "[a-z0-9\\-]+\\([.*]?\\)";

    public static Filter newOperator(String expression, Global global) {
        if (expression.matches(ROOT_FILTER)) {
            return new RootFilter(global);
        }

        if (expression.matches(CURRENT_FILTER)) {
            return new CurrentFilter(global);
        }

        if (expression.matches(CHILD_FILTER)) {
            return new ChildFilter(expression, global);
        }

        if (expression.matches(WILDCARD_FILTER)) {
            return new WildcardFilter(global);
        }

        if (expression.matches(RECURSION_FILTER)) {
            return new RecursionFilter(expression, global);
        }

        if (expression.matches(PREDICATE_FILTER)) {
            return new PredicateFilter(expression.substring(3, expression.length() - 2), global);
        }

        if (expression.matches(UNION_FILTER)) {
            return new UnionFilter(expression.substring(1, expression.length() - 1), global);
        }

        if (expression.matches(SLICE_FILTER)) {
            return new SliceFilter(expression.substring(1, expression.length() - 1), global);
        }

        if (expression.matches(FUNCTION_FILTER)) {
            return new FunctionFilter(expression, global);
        }

        throw new JsonPathExpressionException(expression, 0, "No filter found for expression: " + expression);
    }


}
