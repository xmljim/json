package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;
import io.github.xmljim.json.jsonpath.predicate.expression.TemporalExpression;

class GreaterThanOrEqualPredicate extends AbstractFilterPredicate {
    public GreaterThanOrEqualPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.GREATER_OR_EQUAL_THAN);
    }

    @Override
    public boolean test(Context context) {
        if (leftSide().type().isNumeric() && rightSide().type().isNumeric()) {
            Number left = leftSide().get(context); //(Number) leftSide().getValue(context).orElse(-1);
            Number right = rightSide().get(context); //(Number) rightSide().getValue(context).orElse(0);

            return left.doubleValue() >= right.doubleValue();
        } else if (leftSide().type().isTemporal() && rightSide().type().isTemporal()) {
            long leftValue = ((TemporalExpression) leftSide()).convertToLong(context);
            long rightValue = ((TemporalExpression) rightSide()).convertToLong(context);

            return leftValue >= rightValue;
        } else {
            if ((leftSide().size(context) == 1 && leftSide().getContextType(context).isNumeric())
                    && (rightSide().size(context) == 1 && rightSide().getContextType(context).isNumeric())) {

                Number left = (Number) leftSide().getValue(context).orElse(0);
                Number right = (Number) rightSide().getValue(context).orElse(1);
                return left.doubleValue() >= right.doubleValue();
            } else if ((leftSide().size(context) == 1 && leftSide().getContextType(context).isTemporal())
                    && (rightSide().size(context) == 1 && rightSide().getContextType(context).isTemporal())) {

                long leftValue = ((TemporalExpression) leftSide()).convertToLong(context);
                long rightValue = ((TemporalExpression) rightSide()).convertToLong(context);
                return leftValue >= rightValue;
            }
        }

        return false;
    }

}
