package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.Variables;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static io.xmljim.json.jsonpath.predicate.PredicateOperator.CONTAINS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.ENDS_WITH;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.EQUALS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.GREATER_OR_EQUAL_THAN;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.GREATER_THAN;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.NOT_EQUALS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.REGEX_MATCH;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.STARTS_WITH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredicateFactoryTest {

    @Test
    void testEqualsPredicateNumeric() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEqualsPredicateString() {
        PredicateExpression left = createExpression("'foo'");
        PredicateExpression right = createExpression("'foo'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateNumeric() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, NOT_EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateString() {
        PredicateExpression left = createExpression("'foo'");
        PredicateExpression right = createExpression("'bar'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, NOT_EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanTrue() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalse() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenEqual() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenValuesDifferentTypes() {
        PredicateExpression left = createExpression("5");
        PredicateExpression right = createExpression("'5'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrue() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrueWhenEqual() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalse() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("7");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalseWhenDifferentTypes() {
        PredicateExpression left = createExpression("6");
        PredicateExpression right = createExpression("'6'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsTrue() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Hobbit";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsFalse() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Bilbo";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsWithOrJoinTrue() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Bilbo";
        String leftString2 = "There And Back Again, by Bilbo Baggins";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);

        PredicateExpression left2 = createExpression("'" + leftString2 + "'");
        Predicate<Context> orPredicate = PredicateFactory.create(left2, right, CONTAINS);

        predicate = predicate.or(orPredicate);

        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEndsWithTrue() {
        String leftString = "Jim Earley";
        String rightString = "ley";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, ENDS_WITH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEndsWithFalse() {
        String leftString = "Jim Earley";
        String rightString = "ly";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, ENDS_WITH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    void testStartsWithTrue() {
        String leftString = "Jim Earley";
        String rightString = "Jim";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, STARTS_WITH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    void testStartsWithFalse() {
        String leftString = "Jim Earley";
        String rightString = "Other";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, STARTS_WITH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testRegExpTrue() {
        String leftString = "xml.jim@gmail.com";
        String rightString = "/^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$/i";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, REGEX_MATCH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testRegExpFalse() {
        String leftString = "http://mit.edu";
        String rightString = "/^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$/i";

        PredicateExpression left = createExpression("'" + leftString + "'");
        PredicateExpression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, REGEX_MATCH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    private PredicateExpression createExpression(String expression) {
        return Expression.create(expression, new Variables());
    }


}