package io.github.xmljim.json.jsonpath.predicate;

import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

public interface FilterPredicate {

    Expression leftSide();

    Expression rightSide();

    PredicateOperator operator();
}
