package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

class ContainsPredicate extends StringFilterPredicate {

    public ContainsPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.CONTAINS);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).contains(getValue(rightSide(), context));
    }

}
