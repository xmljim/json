package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.JsonElement;

class ContainsPredicate extends AbstractPredicate {

    public ContainsPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.CONTAINS);
    }

    @Override
    public boolean test(Context context) {
        return getValue(leftSide(), context).contains(getValue(rightSide(), context));
    }

    private String getValue(PredicateExpression expression, Context context) {
        JsonElement element = ((Context) expression.getValue(context)).get();

        if (element.isValue()) {
            return String.valueOf(element.asJsonValue().get());
        } else {
            return element.toJsonString();
        }
    }
}
