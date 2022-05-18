package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.util.DataType;

import java.util.function.Predicate;

/**
 * Factory class with static methods to create different predicate types based on the {@link PredicateOperator}
 */
public class PredicateFactory {

    public static Predicate<Context> create(Expression left, Expression right, PredicateOperator operator) {
        return create(left, right, operator, false);
    }

    public static Predicate<Context> create(Expression left, Expression right, PredicateOperator operator, boolean negate) {
        Predicate<Context> predicate = switch (operator) {
            case CONTAINS -> new ContainsPredicate(left, right);
            case EMPTY -> new EmptyPredicate(left, right);
            case ENDS_WITH -> new EndsWithPredicate(left, right);
            case EQUALS -> new EqualsPredicate(left, right);
            case GREATER_OR_EQUAL_THAN -> new GreaterThanOrEqualPredicate(left, right);
            case GREATER_THAN -> new GreaterThanPredicate(left, right);
            case IN -> new InPredicate(left, right);
            case LESS_OR_EQUAL_THAN -> new LessThanOrEqualPredicate(left, right);
            case LESS_THAN -> new LessThanPredicate(left, right);
            case NOT_EQUALS -> new EqualsPredicate(left, right).negate();
            case NOT_IN -> new InPredicate(left, right).negate();
            case REGEX_MATCH -> new RegExpPredicate(left, right);
            case STARTS_WITH -> new StartsWithPredicate(left, right);
            case IS_NOT_NULL -> new IsNotNullPredicate(left, right);
            default -> checkForIsNotNullPredicate(left);
        };

        if (negate) {
            return predicate.negate();
        }

        return predicate;
    }

    private static Predicate<Context> checkForIsNotNullPredicate(Expression left) {
        if (left.type() == DataType.CONTEXT) {
            return new IsNotNullPredicate(left, null);
        } else {
            throw new JsonPathExpressionException(left.getExpression(), 0, "Unknown Predicate Type");
        }
    }
}
