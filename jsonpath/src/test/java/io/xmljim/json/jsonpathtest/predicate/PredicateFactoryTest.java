package io.xmljim.json.jsonpathtest.predicate;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.PredicateFactory;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.jsonpath.util.Settings;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static io.xmljim.json.jsonpath.predicate.PredicateOperator.CONTAINS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.ENDS_WITH;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.EQUALS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.GREATER_OR_EQUAL_THAN;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.GREATER_THAN;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.IN;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.NOT_EQUALS;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.REGEX_MATCH;
import static io.xmljim.json.jsonpath.predicate.PredicateOperator.STARTS_WITH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredicateFactoryTest {

    @Test
    void testEqualsPredicateNumeric() {
        Expression left = createExpression("5");
        Expression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEqualsPredicateString() {
        Expression left = createExpression("'foo'");
        Expression right = createExpression("'foo'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateNumeric() {
        Expression left = createExpression("5");
        Expression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, NOT_EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testNotEqualsPredicateString() {
        Expression left = createExpression("'foo'");
        Expression right = createExpression("'bar'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, NOT_EQUALS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanTrue() {
        Expression left = createExpression("6");
        Expression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalse() {
        Expression left = createExpression("5");
        Expression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenEqual() {
        Expression left = createExpression("5");
        Expression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanFalseWhenValuesDifferentTypes() {
        Expression left = createExpression("5");
        Expression right = createExpression("'5'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrue() {
        Expression left = createExpression("6");
        Expression right = createExpression("5");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualTrueWhenEqual() {
        Expression left = createExpression("6");
        Expression right = createExpression("6");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalse() {
        Expression left = createExpression("6");
        Expression right = createExpression("7");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testGreaterThanOrEqualFalseWhenDifferentTypes() {
        Expression left = createExpression("6");
        Expression right = createExpression("'6'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, GREATER_OR_EQUAL_THAN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsTrue() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Hobbit";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsFalse() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Bilbo";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testContainsWithOrJoinTrue() {
        String leftString = "The Hobbit: An Unexpected Journey";
        String rightString = "Bilbo";
        String leftString2 = "There And Back Again, by Bilbo Baggins";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, CONTAINS);

        Expression left2 = createExpression("'" + leftString2 + "'");
        Predicate<Context> orPredicate = PredicateFactory.create(left2, right, CONTAINS);

        predicate = predicate.or(orPredicate);

        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEndsWithTrue() {
        String leftString = "Jim Earley";
        String rightString = "ley";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, ENDS_WITH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testEndsWithFalse() {
        String leftString = "Jim Earley";
        String rightString = "ly";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, ENDS_WITH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testStartsWithTrue() {
        String leftString = "Jim Earley";
        String rightString = "Jim";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, STARTS_WITH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testStartsWithFalse() {
        String leftString = "Jim Earley";
        String rightString = "Other";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression("'" + rightString + "'");
        Predicate<Context> predicate = PredicateFactory.create(left, right, STARTS_WITH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testRegExpTrue() {
        String leftString = "xml.jim@gmail.com";
        String rightString = "/^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$/i";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, REGEX_MATCH);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testRegExpFalse() {
        String leftString = "http://mit.edu";
        String rightString = "/^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$/i";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, REGEX_MATCH);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    @Test
    void testInPredicateTrue() {
        String leftString = "a";
        String rightString = "['a', 'b', 'd']";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, IN);
        assertNotNull(predicate);
        assertTrue(predicate.test(Context.defaultContext()));
    }

    @Test
    void testInPredicateFalse() {
        String leftString = "c";
        String rightString = "['a', 'b', 'd']";

        Expression left = createExpression("'" + leftString + "'");
        Expression right = createExpression(rightString);
        Predicate<Context> predicate = PredicateFactory.create(left, right, IN);
        assertNotNull(predicate);
        assertFalse(predicate.test(Context.defaultContext()));
    }

    void testEmptyTrue() {

    }

    private Expression createExpression(String expression) {
        return ExpressionFactory.create(expression, new Settings());
    }

    private JsonObject createJsonObject() {
        ElementFactory factory = ServiceManager.getProvider(ElementFactory.class);
        return factory.newObject();
    }

    private JsonArray createJsonArray() {
        ElementFactory factory = ServiceManager.getProvider(ElementFactory.class);
        return factory.newArray();
    }

}