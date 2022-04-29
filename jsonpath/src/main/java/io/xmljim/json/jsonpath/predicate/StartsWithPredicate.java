package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

class StartsWithPredicate extends StringFilterPredicate {
    public StartsWithPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.STARTS_WITH);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).startsWith(getValue(rightSide(), context));
    }
}
