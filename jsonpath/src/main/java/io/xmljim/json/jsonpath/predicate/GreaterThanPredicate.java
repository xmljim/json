package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

class GreaterThanPredicate extends AbstractFilterPredicate {
    public GreaterThanPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.GREATER_THAN);
    }

    @Override
    public boolean test(Context context) {
        if (leftSide().type().isNumeric() && rightSide().type().isNumeric()) {
            Number left = (Number) leftSide().getValue(context).orElse(-1);
            Number right = (Number) rightSide().getValue(context).orElse(0);

            return left.doubleValue() > right.doubleValue();
        } else {
            if ((leftSide().size(context) == 1 && leftSide().getContextType(context).isNumeric())
                && (rightSide().size(context) == 1 && rightSide().getContextType(context).isNumeric())) {
                Number left = (Number) leftSide().getValue(context).orElse(0);
                Number right = (Number) rightSide().getValue(context).orElse(1);
                return left.doubleValue() > right.doubleValue();
            }
        }

        return false;
    }
}