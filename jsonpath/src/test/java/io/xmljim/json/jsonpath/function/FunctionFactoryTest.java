package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.variables.Variables;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionFactoryTest {

    @Test
    void testCreateFunction() {
        String expr = "sum()";
        JsonPathFunction function = FunctionFactory.createFunction(expr, new Variables());
        assertEquals("sum", function.name());
    }

    @Test
    void testNoFunctionAvailable() {
        String expr = "foo(bar)";
        assertThrows(JsonPathException.class, () -> FunctionFactory.createFunction(expr, new Variables()));
    }

    @Test
    void testValidFunctionNameInvalidArgs() {
        String expr = "sum(1,2)";
        assertThrows(JsonPathException.class, () -> FunctionFactory.createFunction(expr, new Variables()));
    }

    @Test
    void testFunctionWithArgs() {
        String expr = "substring(1, 4)";
        JsonPathFunction function = FunctionFactory.createFunction(expr, new Variables());
        assertEquals("substring", function.name());
    }
}