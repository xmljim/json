package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

class LessThanPredicate extends AbstractFilterPredicate {
    public LessThanPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.LESS_THAN);
    }

    @Override
    public boolean test(Context context) {
        if (leftSide().type().isNumeric() && rightSide().type().isNumeric()) {
            Number left = (Number) leftSide().getValue(context).orElse(0);
            Number right = (Number) rightSide().getValue(context).orElse(-1);

            return left.doubleValue() < right.doubleValue();
        } else {


            if ((leftSide().getContextType(context).isNumeric())
                && (rightSide().getContextType(context).isNumeric())) {

                Number left = (Number) leftSide().getValue(context).orElse(0);
                Number right = (Number) rightSide().getValue(context).orElse(-1);

                return left.doubleValue() < right.doubleValue();
            }
        }

        return false;
    }
}
