package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

class GreaterThanPredicate extends AbstractPredicate {
    public GreaterThanPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.GREATER_THAN);
    }

    @Override
    public boolean test(Context context) {

        if (leftSide().type().isNumeric() && rightSide().type().isNumeric()) {
            Number left = leftSide().getValue(context);
            Number right = rightSide().getValue(context);

            return left.doubleValue() > right.doubleValue();
        } else {
            Context leftContext = leftSide().get(context);
            Context rightContext = rightSide().get(context);

            if (leftContext.type().isNumeric() && rightContext.type().isNumeric()) {
                Number left = leftSide().getValue(context);
                Number right = rightSide().getValue(context);
                return left.doubleValue() > right.doubleValue();
            }
        }

        return false;
    }
}
