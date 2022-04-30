package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

/**
 * Extension class for retrieving string values
 */
abstract class StringFilterPredicate extends AbstractFilterPredicate {
    public StringFilterPredicate(PredicateExpression leftSide, PredicateExpression rightSide, PredicateOperator operator) {
        super(leftSide, rightSide, operator);
    }

    public String getValue(PredicateExpression expression, Context context) {
        return (String) expression.getValue(context).orElse("");
    }
}
