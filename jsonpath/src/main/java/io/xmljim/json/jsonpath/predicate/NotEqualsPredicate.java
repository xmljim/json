package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

class NotEqualsPredicate extends AbstractFilterPredicate {

    public NotEqualsPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.NOT_EQUALS);
    }

    @Override
    public boolean test(Context context) {
        return !leftSide().getValue(context).equals(rightSide().getValue(context));
    }
}
