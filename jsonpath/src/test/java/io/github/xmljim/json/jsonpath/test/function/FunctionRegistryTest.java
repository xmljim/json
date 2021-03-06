package io.github.xmljim.json.jsonpath.test.function;

import io.github.xmljim.json.jsonpath.function.FunctionRegistry;
import io.github.xmljim.json.jsonpath.function.aggregate.AvgFunction;
import io.github.xmljim.json.jsonpath.function.aggregate.SumFunction;
import io.github.xmljim.json.jsonpath.function.node.IsNumericFunction;
import io.github.xmljim.json.jsonpath.function.node.IsPrimitiveFunction;
import io.github.xmljim.json.jsonpath.function.node.NodeTypeFunction;
import io.github.xmljim.json.jsonpath.util.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionRegistryTest {

    @Test
    void testAdd() {
        FunctionRegistry registry = new FunctionRegistry();
        registry.registerFunction(AvgFunction.class);
        registry.registerFunction(SumFunction.class);
        registry.registerFunction(IsNumericFunction.class);
        registry.registerFunction(IsPrimitiveFunction.class);
        registry.registerFunction(NodeTypeFunction.class);
        assertTrue(registry.containsFunction("average"));

        System.out.println(registry.printFunctionList());
    }

    @Test
    void testFromGlobal() {
        Settings settings = new Settings();
        System.out.println(settings.getFunctionRegistry().printFunctionList());
    }

}