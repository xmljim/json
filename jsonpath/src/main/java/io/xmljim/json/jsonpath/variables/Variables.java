package io.xmljim.json.jsonpath.variables;

import io.xmljim.json.jsonpath.function.FunctionRegistry;
import io.xmljim.json.jsonpath.function.math.AvgFunction;
import io.xmljim.json.jsonpath.function.math.SumFunction;
import io.xmljim.json.jsonpath.function.node.IsNumericFunction;
import io.xmljim.json.jsonpath.function.node.IsPrimitiveFunction;
import io.xmljim.json.jsonpath.function.node.NodeTypeFunction;
import io.xmljim.json.jsonpath.function.string.SubstringFunction;
import io.xmljim.json.jsonpath.function.string.UpperCaseFunction;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.model.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Variables implements Global {
    private final Map<String, Expression> variables = new HashMap<>();
    private final Map<String, Object> properties = new HashMap<>();
    private final FunctionRegistry registry = new FunctionRegistry();

    public Variables() {
        registerInternalFunctions();
    }

    private void registerInternalFunctions() {
        registry.registerFunction(SumFunction.class);
        registry.registerFunction(AvgFunction.class);
        registry.registerFunction(NodeTypeFunction.class);
        registry.registerFunction(IsPrimitiveFunction.class);
        registry.registerFunction(IsNumericFunction.class);
        registry.registerFunction(UpperCaseFunction.class);
        registry.registerFunction(SubstringFunction.class);
    }

    public void setVariable(String name, Expression expression) {
        variables.putIfAbsent(name, expression);
    }

    public void setVariable(String name, Number number) {
        setVariable(name, ExpressionFactory.create(number.toString(), this));
    }

    public void setVariable(String name, String stringValue) {
        setVariable(name, ExpressionFactory.create("'" + stringValue + "'", this));
    }

    public void setVariable(String name, Boolean booleanValue) {
        setVariable(name, ExpressionFactory.create(booleanValue.toString(), this));
    }

    public void setVariable(String name, JsonNode node) {
        setVariable(name, ExpressionFactory.withJsonNode(node, this));
    }

    public <T> void setProperty(String name, T value) {
        properties.put(name, value);
    }


    @Override
    public Expression getVariable(String name) {
        return variables.getOrDefault(name, ExpressionFactory.create("null", this));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    @Override
    public FunctionRegistry getFunctionRegistry() {
        return registry;
    }
}
