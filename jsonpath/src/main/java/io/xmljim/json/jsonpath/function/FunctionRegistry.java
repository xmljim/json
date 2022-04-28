package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {

    private final Map<String, Class<? extends JsonPathFunction<?, ?>>> registry = new HashMap<>();

    public void registerFunction(String name, Class<? extends JsonPathFunction<?, ?>> functionClass) {
        registry.put(name, functionClass);
    }

    public <T extends JsonPathFunction<?, ?>> T newInstance(String name, PredicateExpression... arguments) {
        return null;
    }
}
