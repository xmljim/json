package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

class GreaterThanPredicate extends AbstractFilterPredicate {
    public GreaterThanPredicate(Expression leftSide, Expression rightSide) {
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
