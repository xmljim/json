package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.model.JsonValue;

class EqualsPredicate extends AbstractFilterPredicate {
    public EqualsPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.EQUALS);
    }


    @Override
    public boolean test(Context context) {
        Context l = leftSide().getContext(context).orElse(Context.defaultContext());
        Context r = rightSide().getContext(context).orElse(Context.defaultContext());


        JsonValue<?> left = l.get().asJsonValue();
        JsonValue<?> right = r.get().asJsonValue();
        return left.equals(right);
    }

}
