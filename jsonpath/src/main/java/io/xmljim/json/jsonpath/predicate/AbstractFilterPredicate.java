package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

import java.util.function.Predicate;

abstract class AbstractFilterPredicate implements Predicate<Context>, FilterPredicate {

    private final Expression leftSide;
    private final Expression rightSide;
    private final PredicateOperator operator;

    public AbstractFilterPredicate(Expression leftSide, Expression rightSide, PredicateOperator operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    @Override
    public Expression leftSide() {
        return leftSide;
    }

    @Override
    public Expression rightSide() {
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
