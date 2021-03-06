package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;
import io.github.xmljim.json.jsonpath.util.DataType;

class EmptyPredicate extends AbstractFilterPredicate {
    public EmptyPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.EMPTY);
    }

    @Override
    public boolean test(Context context) {
        String unexpected = "Unexpected Error";
        if (rightSide().getContextType(context) != DataType.BOOLEAN) {
            throw new JsonPathExpressionException(rightSide().toString(), 0, "Expected a boolean test expression. Value argType is" + rightSide().getContextType(context));
        } else {
            boolean rightValue = (boolean) rightSide().getValue(context)
                .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, unexpected)); //shouldn't get here

            DataType leftType = leftSide().getContextType(context);

            if (leftSide().isEmpty(context)) {
                return rightValue;
            } else {
                if (leftType == DataType.STRING) {
                    return String.valueOf(leftSide().getValue(context).orElse("")).isEmpty() == rightValue;
                } else if (leftType.isArray() && leftSide().size(context) == 1) {
                    return leftSide().getContext(context).map(c -> context.get().asJsonArray().isEmpty() == rightValue)
                        .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, unexpected));
                } else if (leftType.isObject() && leftSide().size(context) == 1) {
                    return leftSide().getContext(context).map(c -> context.get().asJsonObject().isEmpty() == rightValue)
                        .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, unexpected));
                } else {
                    return !rightValue;
                }
            }
        }
    }


}
