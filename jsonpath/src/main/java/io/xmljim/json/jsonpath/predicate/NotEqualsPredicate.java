package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

class NotEqualsPredicate extends AbstractFilterPredicate {

    public NotEqualsPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.NOT_EQUALS);
    }

    @Override
    public boolean test(Context context) {
        return !leftSide().getValue(context).equals(rightSide().getValue(context));
    }
}
