package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

abstract class AbstractPredicate implements FilterPredicate {

    private final PredicateExpression leftSide;
    private final PredicateExpression rightSide;
    private final PredicateOperator operator;

    public AbstractPredicate(PredicateExpression leftSide, PredicateExpression rightSide, PredicateOperator operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    @Override
    public PredicateExpression leftSide() {
        return leftSide;
    }

    @Override
    public PredicateExpression rightSide() {
        return rightSide;
    }

    @Override
    public PredicateOperator operator() {
        return operator;
    }
}
