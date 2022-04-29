package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.JsonElement;

/**
 * Extension class for retrieving string values
 */
abstract class StringFilterPredicate extends FilterPredicate {
    public StringFilterPredicate(PredicateExpression leftSide, PredicateExpression rightSide, PredicateOperator operator) {
        super(leftSide, rightSide, operator);
    }

    public String getValue(PredicateExpression expression, Context context) {
        JsonElement element = expression.get(context).get();

        if (element.isValue()) {
            return String.valueOf(element.asJsonValue().get());
        } else {
            return element.toJsonString();
        }
    }
}
