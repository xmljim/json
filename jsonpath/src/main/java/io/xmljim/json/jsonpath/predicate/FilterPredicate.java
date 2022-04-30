package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

public interface FilterPredicate {

    PredicateExpression leftSide();

    PredicateExpression rightSide();

    PredicateOperator operator();
}
