package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonValue;

class InPredicate extends AbstractFilterPredicate {
    public InPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.IN);
    }

    @Override
    public boolean test(Context context) {
        if (rightSide().getContextType(context).isArray()) {
            if (leftSide().isEmpty(context)) {
                return false;
            } else {
                JsonArray right = (JsonArray) rightSide().getValue(context).orElseThrow();
                JsonValue<?> left = ensureAsJsonValue(context);
                assert right != null;
                return right.containsValue(left);
            }
        }

        return false;
    }

    private JsonValue<?> ensureAsJsonValue(Context context) {
        Object leftValue = leftSide().getValue(context).orElse(null);

        if (leftValue instanceof JsonValue<?> jsonValue) {
            return jsonValue;
        } else {
            return Context.createSimpleContext(leftValue).get().asJsonValue();
        }
    }
}
