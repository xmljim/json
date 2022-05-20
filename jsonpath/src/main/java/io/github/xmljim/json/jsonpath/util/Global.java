package io.github.xmljim.json.jsonpath.util;

import io.github.xmljim.json.jsonpath.function.FunctionRegistry;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;

public interface Global {
    Expression getVariable(String name);

    <T> T getProperty(String name);

    FunctionRegistry getFunctionRegistry();
}
