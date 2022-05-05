package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.model.NodeType;

class EmptyPredicate extends AbstractFilterPredicate {
    public EmptyPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.EMPTY);
    }

    @Override
    public boolean test(Context context) {
        if (rightSide().getContextType(context) != NodeType.BOOLEAN) {
            throw new JsonPathExpressionException(rightSide().toString(), 0, "Expected a boolean test expression. Value type is" + rightSide().getContextType(context));
        } else {
            boolean rightValue = (boolean) rightSide().getValue(context)
                .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, "Unexpected Error")); //shouldn't get here

            NodeType leftType = leftSide().getContextType(context);

            if (leftSide().isEmpty(context)) {
                return rightValue;
            } else {
                if (leftType == NodeType.STRING) {
                    return String.valueOf(leftSide().getValue(context).orElse("")).isEmpty() == rightValue;
                } else if (leftType == NodeType.ARRAY && leftSide().size(context) == 1) {
                    return leftSide().getContext(context).map(c -> context.get().asJsonArray().isEmpty() == rightValue)
                        .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, "Unexpected Error"));
                } else if (leftType == NodeType.OBJECT && leftSide().size(context) == 1) {
                    return leftSide().getContext(context).map(c -> context.get().asJsonObject().isEmpty() == rightValue)
                        .orElseThrow(() -> new JsonPathExpressionException(toString(), 0, "Unexpected Error"));
                } else {
                    return !rightValue;
                }
            }
        }
    }


}
