package io.xmljim.json.jsonpathtest.function;

import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.function.FunctionFactory;
import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.jsonpath.function.PredicateArgument;
import io.xmljim.json.jsonpath.util.Settings;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FunctionFactoryTest {

    @Test
    @Order(1)
    void testCreateFunction() {
        String expr = "sum()";
        JsonPathFunction function = FunctionFactory.createFunction(expr, new Settings());
        assertEquals("sum", function.name());
    }

    @Test
    @Order(2)
    void testNoFunctionAvailable() {
        String expr = "foo(bar)";
        assertThrows(JsonPathException.class, () -> FunctionFactory.createFunction(expr, new Settings()));
    }

    @Test
    @Order(3)
    void testValidFunctionNameInvalidArgs() {
        String expr = "sum(1,2)";
        assertThrows(JsonPathException.class, () -> FunctionFactory.createFunction(expr, new Settings()));
    }

    @Test
    @Order(4)
    void testFunctionWithArgs() {
        String expr = "substring(1, 4)";
        JsonPathFunction function = FunctionFactory.createFunction(expr, new Settings());
        assertEquals("substring", function.name());
        assertEquals(2, function.arguments().size());
        assertTrue(function.hasArgument("start"));
        assertTrue(function.hasArgument("end"));
    }

    @Test
    @Order(5)
    void testFunctionWithPredicateFunction() {
        String expr = "count-if(@.assignments.*, @.doc_id[1:3] == 19 && @.assignments.doc_status == 'PROCESSED')";
        JsonPathFunction function = FunctionFactory.createFunction(expr, new Settings());
        assertNotNull(function);
        assertEquals("count-if", function.name());
        assertEquals(2, function.arguments().size());
        assertTrue(function.hasArgument("test"));
        assertTrue(function.getArgument("test").orElseThrow() instanceof PredicateArgument);
    }
}