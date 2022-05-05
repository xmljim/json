package io.xmljim.json.jsonpath.variables;

import io.xmljim.json.jsonpath.function.FunctionRegistry;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

public interface Global {
    Expression getVariable(String name);

    <T> T getProperty(String name);

    FunctionRegistry getFunctionRegistry();
}
