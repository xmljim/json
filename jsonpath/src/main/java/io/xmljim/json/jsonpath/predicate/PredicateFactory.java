package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.function.Predicate;

public class PredicateFactory {

    public static Predicate<Context> create(PredicateExpression left, PredicateExpression right, PredicateOperator operator) {
        return create(left, right, operator, false);
    }

    public static Predicate<Context> create(PredicateExpression left, PredicateExpression right, PredicateOperator operator, boolean negate) {
        Predicate<Context> predicate = switch (operator) {
            case CONTAINS -> new ContainsPredicate(left, right);
            case ENDS_WITH -> new EndsWithPredicate(left, right);
            case EQUALS -> new EqualsPredicate(left, right);
            case GREATER_OR_EQUAL_THAN -> new GreaterThanOrEqualPredicate(left, right);
            case GREATER_THAN -> new GreaterThanPredicate(left, right);
            case IN -> new InPredicate(left, right);
            case LESS_OR_EQUAL_THAN -> new LessThanOrEqualPredicate(left, right);
            case LESS_THAN -> new LessThanPredicate(left, right);
            case NOT_EQUALS -> new NotEqualsPredicate(left, right);
            case NOT_IN -> new InPredicate(left, right).negate();
            case REGEX_MATCH -> new RegExpPredicate(left, right);
            case STARTS_WITH -> new StartsWithPredicate(left, right);
            default -> null;
        };

        if (negate) {
            return predicate.negate();
        }

        return predicate;
    }
}
