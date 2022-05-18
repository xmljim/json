package io.xmljim.json.jsonpathtest.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;
import io.xmljim.json.jsonpath.util.Settings;
import io.xmljim.json.model.JsonArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionTest {

    @Test
    void testStringExpression() {
        String val = "Testing";
        String expr = "'" + val + "'";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        Assertions.assertEquals(DataType.STRING, expression.type());

        assertEquals(val, expression.getValue(Context.defaultContext()).orElse(null));
    }

    @Test
    void testBooleanExpression() {
        String expr = "true";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.BOOLEAN, expression.type());
        assertEquals(Boolean.parseBoolean(expr), expression.getValue(Context.defaultContext()).orElseThrow());

        expr = "false";
        expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.BOOLEAN, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertEquals(Boolean.parseBoolean(expr), expression.getValue(Context.defaultContext()).orElseThrow());
    }

    @Test
    void testIntegerExpression() throws Exception {
        int val = 1234;
        String expr = String.valueOf(val);
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.INTEGER, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, (Integer) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Expected integer value")));
    }

    @Test
    void testLongExpression() throws Exception {
        long val = Long.MAX_VALUE;
        String expr = String.valueOf(val);
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.LONG, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, (Long) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Expected long value")));
    }

    @Test
    void testDoubleExpression() throws Exception {
        double val = 3.1415927;
        String expr = String.valueOf(val);
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.DOUBLE, expression.type());
        assertTrue(expression.type().isPrimitive());
        assertTrue(expression.type().isNumeric());
        assertEquals(val, expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Expected double value")));
    }

    @Test
    void testNullExpression() {
        String expr = "null";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.NULL, expression.type());
        assertTrue(expression.getValue(Context.defaultContext()).isEmpty());
    }

    @Test
    void testRegexExpression() {
        String expr = "/[$|@]((\\.)?(\\[?.*?]?))*/i";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.REGEX, expression.type());
    }

    @Test
    void testListExpression() throws Exception {
        String expr = "[1,'2',true]";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.LIST, expression.type());
        assertEquals(DataType.ARRAY, expression.getContextType(Context.defaultContext()));
        assertTrue(expression.getValue(Context.defaultContext()).orElse(null) instanceof JsonArray);
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Unexpected error - List expression"))).contains(1));
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Unexpected error - List expression"))).contains(true));
        assertTrue(((JsonArray) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Unexpected error - List expression"))).contains("2"));
        assertFalse(((JsonArray) expression.getValue(Context.defaultContext()).orElseThrow(() -> new Exception("Unexpected error - List expression"))).contains("foo"));
    }

    @Test
    void testPathPredicateExpression() {
        String expr = "@.foo[1]..*[?($.v == 'foo')]";
        Expression expression = createDefaultExpression(expr);
        assertNotNull(expression);
        assertEquals(DataType.CONTEXT, expression.type());
    }

    @Test
    void testVariableExpressionSimple() {
        Settings settings = new Settings();
        settings.setVariable("C", 100);
        settings.setVariable("name", "Jim");
        settings.setVariable("bool", true);

        String expr = "{C}";
        Expression expression = createExpressionWithVariables(expr, settings);
        assertNotNull(expression);
        assertEquals(DataType.VARIABLE, expression.type());
        assertEquals(100, expression.getValue(Context.defaultContext()).orElseThrow());

        expr = "{name}";
        expression = createExpressionWithVariables(expr, settings);
        assertNotNull(expression);
        assertEquals(DataType.VARIABLE, expression.type());
        assertEquals("Jim", expression.getValue(Context.defaultContext()).orElseThrow());

        expr = "{bool}";
        expression = createExpressionWithVariables(expr, settings);
        assertNotNull(expression);
        assertEquals(DataType.VARIABLE, expression.type());
        assertTrue((Boolean) expression.getValue(Context.defaultContext()).orElseThrow());

    }

    private Expression createDefaultExpression(String expression) {
        return ExpressionFactory.create(expression, new Settings());
    }

    private Expression createExpressionWithVariables(String expression, Global global) {
        return ExpressionFactory.create(expression, global);
    }
}