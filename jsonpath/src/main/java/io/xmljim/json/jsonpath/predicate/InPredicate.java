package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonValue;

class InPredicate extends AbstractFilterPredicate {
    public InPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.IN);
    }

    @Override
    public boolean test(Context context) {
        if (rightSide().getContextType(context).isArray()) {
            if (leftSide().isEmpty(context)) {
                return false;
            } else {
                Context ctx = rightSide().getContext(context).orElse(null);
                if (ctx != null) {
                    JsonElement element = ctx.get();
                    JsonArray right = element.asJsonArray();
                    JsonValue<?> left = ensureAsJsonValue(context);
                    return right.containsValue(left);
                }

                return false;
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
