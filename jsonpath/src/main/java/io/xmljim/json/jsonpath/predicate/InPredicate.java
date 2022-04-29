package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.NodeType;

class InPredicate extends FilterPredicate {
    public InPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.IN);
    }

    @Override
    public boolean test(Context context) {
        Context rightContext = rightSide().get(context);
        Context leftContext = leftSide().get(context);
        if (rightContext.type() == NodeType.ARRAY) {
            return rightContext.get().asJsonArray().contains(leftContext.get().asJsonValue().get());
        }

        return false;
    }
}
