package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

class StartsWithPredicate extends StringFilterPredicate {
    public StartsWithPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.STARTS_WITH);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).startsWith(getValue(rightSide(), context));
    }
}
