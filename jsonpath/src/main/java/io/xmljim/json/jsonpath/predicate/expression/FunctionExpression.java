package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.FunctionFactory;
import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class FunctionExpression extends AbstractExpression {
    private final JsonPathFunction function;
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public FunctionExpression(String expression, Global global) {
        super(expression, global);
        this.function = FunctionFactory.createFunction(expression, global);
    }

    public int size(Context inputContext) {
        //System.out.println("size [" + getExpression() + "]: " + inputContext);
        applyContext(inputContext);
        return values(inputContext).size();
    }

    public List<Context> values(Context inputContext) {
        String key = function.name() + "_" + getExpression() + "_" + inputContext.toString();
        //System.out.println("Get Cache [" + key + "]");
        return concurrentHashMap.getOrDefault(key, Collections.emptyList());
    }


    void applyContext(Context inputContext) {
        if (!isCached(inputContext)) {
            cache(inputContext, function.apply(Stream.of(inputContext)).toList());
        }
    }


    void cache(Context inputContext, List<Context> values) {
        String key = function.name() + "_" + getExpression() + "_" + inputContext.toString();
        System.out.println("Cache key: " + key);
        concurrentHashMap.putIfAbsent(key, values);
        //System.out.println("Cached [" + key + "]: " + values);
    }

    boolean isCached(Context inputContext) {
        String key = getExpression() + "_" + inputContext.toString();
        return concurrentHashMap.containsKey(key);
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        if (!isCached(inputContext)) {
            applyContext(inputContext);
        }

        List<Context> contexts = values(inputContext);
        if (index <= contexts.size() - 1) {
            return Optional.of(contexts.get(0));
        }
        return Optional.empty();
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.FUNCTION;
    }
}
