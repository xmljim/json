package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.Variables;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.NodeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @Test
    void testStringExpression() {
        String val = "Testing";
        String expr = "'" + val + "'";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.STRING, expression.type());

        assertEquals(val, expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testBooleanExpression() {
        String expr = "true";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.BOOLEAN, expression.type());
        assertEquals(Boolean.parseBoolean(expr), expression.getValue(Context.createSimpleContext(null)));

        expr = "false";
        expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.BOOLEAN, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertEquals(Boolean.parseBoolean(expr), expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testIntegerExpression() {
        int val = 1234;
        String expr = String.valueOf(val);
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.INTEGER, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, (Integer) expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testLongExpression() {
        long val = Long.MAX_VALUE;
        String expr = String.valueOf(val);
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.LONG, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, (Long) expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testDoubleExpression() {
        double val = 3.1415927;
        String expr = String.valueOf(val);
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.DOUBLE, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testNullExpression() {
        String expr = "null";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.NULL, expression.type());
        assertNull(expression.getValue(Context.createSimpleContext(null)));
    }

    @Test
    void testRegexExpression() {
        String expr = "/[$|@]((\\.)?(\\[?.*?]?))*/i";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.REGEX, expression.type());
    }

    @Test
    void testListExpression() {
        String expr = "[1,'2',true]";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.LIST, expression.type());
        assertEquals(NodeType.ARRAY, expression.get(Context.createSimpleContext(null)).type());
        assertTrue(expression.getValue(Context.createSimpleContext(null)) instanceof JsonArray);
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext())).contains(1));
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext())).contains(true));
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext())).contains("2"));
        assertFalse(((JsonArray) expression.getValue(Context.defaultContext())).contains("foo"));
    }

    @Test
    void testPathPredicateExpression() {
        String expr = "@.foo[1]..*[?($.v == 'foo')]";
        PredicateExpression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(ExpressionType.CONTEXT, expression.type());
    }

    @Test
    void testVariableExpressionSimple() {
        Variables variables = new Variables();
        variables.setVariable("C", 100);
        variables.setVariable("name", "Jim");
        variables.setVariable("bool", true);

        String expr = "{C}";
        PredicateExpression expression = createExpressionWithVariables(expr, variables);
        assertNotNull(expression);
        assertEquals(ExpressionType.VARIABLE, expression.type());
        assertEquals((Integer) 100, expression.getValue(Context.defaultContext()));

        expr = "{name}";
        expression = createExpressionWithVariables(expr, variables);
        assertNotNull(expression);
        assertEquals(ExpressionType.VARIABLE, expression.type());
        assertEquals("Jim", expression.getValue(Context.defaultContext()));

        expr = "{bool}";
        expression = createExpressionWithVariables(expr, variables);
        assertNotNull(expression);
        assertEquals(ExpressionType.VARIABLE, expression.type());
        assertTrue((Boolean) expression.getValue(Context.defaultContext()));

    }

    private PredicateExpression createDefaultExpression(String expression) {
        return Expression.create(expression, new Variables());
    }

    private PredicateExpression createExpressionWithVariables(String expression, Global global) {
        return Expression.create(expression, global);
    }
}