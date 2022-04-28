package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.Variables;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredicateFactoryTest {

    @Test
    void testEqualsPredicateNumeric() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("5");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.EQUALS, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEqualsPredicateString() {
        PredicateExpression left = createExpression("'foo'");
        PredicateExpression right = createExpression("'foo'");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.EQUALS, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateNumeric() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("6");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.NOT_EQUALS, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateString() {
        PredicateExpression left = createExpression("'foo'");
        PredicateExpression right = createExpression("'bar'");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.NOT_EQUALS, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanTrue() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("5");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_THAN, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalse() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("6");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_THAN, false);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenEqual() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("5");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_THAN, false);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenValuesDifferentTypes() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("'5'");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_THAN, false);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrue() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("5");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_OR_EQUAL_THAN, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrueWhenEqual() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("6");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_OR_EQUAL_THAN, false);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalse() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("7");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_OR_EQUAL_THAN, false);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalseWhenDifferentTypes() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("'6'");
        FilterPredicate predicate = PredicateFactory.create(left, right, PredicateOperator.GREATER_OR_EQUAL_THAN, false);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    private PredicateExpression createExpression(String expression) {
        return Expression.create(expression, new Variables());
    }
}