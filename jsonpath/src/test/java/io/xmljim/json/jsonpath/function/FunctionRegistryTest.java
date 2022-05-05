package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.function.math.AvgFunction;
import io.xmljim.json.jsonpath.function.math.SumFunction;
import io.xmljim.json.jsonpath.function.node.IsNumericFunction;
import io.xmljim.json.jsonpath.function.node.IsPrimitiveFunction;
import io.xmljim.json.jsonpath.function.node.NodeTypeFunction;
import io.xmljim.json.jsonpath.variables.Variables;
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
        Variables variables = new Variables();
        System.out.println(variables.getFunctionRegistry().printFunctionList());
    }

}