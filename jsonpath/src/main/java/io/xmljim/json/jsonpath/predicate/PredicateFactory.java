package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

public class PredicateFactory {

    public static FilterPredicate create(PredicateExpression left, PredicateExpression right, PredicateOperator operator, boolean negate) {
        FilterPredicate predicate = switch (operator) {
            case EQUALS -> new EqualsPredicate(left, right);
            case NOT_EQUALS -> new NotEqualsPredicate(left, right);
            case CONTAINS -> new ContainsPredicate(left, right);
            case GREATER_THAN -> new GreaterThanPredicate(left, right);
            case GREATER_OR_EQUAL_THAN -> new GreaterThanOrEqualPredicate(left, right);
            case LESS_THAN -> new LessThanPredicate(left, right);
            case LESS_OR_EQUAL_THAN -> new LessThanOrEqualPredicate(left, right);
            default -> null;
        };

        if (predicate != null && negate) {
            return (FilterPredicate) predicate.negate();
        } else {
            return predicate;
        }
    }
}
