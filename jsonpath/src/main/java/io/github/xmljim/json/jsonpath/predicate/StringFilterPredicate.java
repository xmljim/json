package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

/**
 * Extension class for retrieving string values
 */
abstract class StringFilterPredicate extends AbstractFilterPredicate {
    public StringFilterPredicate(Expression leftSide, Expression rightSide, PredicateOperator operator) {
        super(leftSide, rightSide, operator);
    }

    public String getValue(Expression expression, Context context) {
        return (String) expression.getValue(context).orElse("");
    }
}
