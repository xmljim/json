package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

class EndsWithPredicate extends StringFilterPredicate {
    public EndsWithPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.ENDS_WITH);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).contains(getValue(rightSide(), context));
    }

}
