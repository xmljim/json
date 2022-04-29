package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

class EndsWithPredicate extends StringFilterPredicate {
    public EndsWithPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.ENDS_WITH);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).contains(getValue(rightSide(), context));
    }

}
