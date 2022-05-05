package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

class EqualsPredicate extends AbstractFilterPredicate {
    public EqualsPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.EQUALS);
    }


    @Override
    public boolean test(Context context) {
        System.out.println(leftSide().getValue(context));
        System.out.println(rightSide().getValue(context));

        System.out.println(leftSide().getValue(context).equals(rightSide().getValue(context)));
        return leftSide().getValue(context).equals(rightSide().getValue(context));
    }

}
