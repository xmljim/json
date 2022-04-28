package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.function.Predicate;

public interface FilterPredicate extends Predicate<Context> {
    PredicateOperator operator();

    PredicateExpression leftSide();

    PredicateExpression rightSide();
}
