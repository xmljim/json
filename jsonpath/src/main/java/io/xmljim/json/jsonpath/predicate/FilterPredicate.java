package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.function.Predicate;

abstract class FilterPredicate implements Predicate<Context> {

    private final PredicateExpression leftSide;
    private final PredicateExpression rightSide;
    private final PredicateOperator operator;

    public FilterPredicate(PredicateExpression leftSide, PredicateExpression rightSide, PredicateOperator operator) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.operator = operator;
    }

    public abstract boolean test(Context context);

    public PredicateExpression leftSide() {
        return leftSide;
    }

    public PredicateExpression rightSide() {
        return rightSide;
    }

    public PredicateOperator operator() {
        return operator;
    }

    public String toString() {
        return leftSide + " " + operator.getOperator() + " " + rightSide;
    }


}
