package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.function.Predicate;

abstract class AbstractFilterPredicate implements Predicate<Context>, FilterPredicate {

    private final PredicateExpression leftSide;
    private final PredicateExpression rightSide;
    private final PredicateOperator operator;

    public AbstractFilterPredicate(PredicateExpression leftSide, PredicateExpression rightSide, PredicateOperator operator) {
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

    @Override
    public String toString() {
        return leftSide + " " + operator.getOperator() + " " + rightSide;
    }
}
