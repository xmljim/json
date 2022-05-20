package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.function.FunctionFactory;
import io.github.xmljim.json.jsonpath.function.JsonPathFunction;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class FunctionExpression extends CachedExpression {
    private final JsonPathFunction function;
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public FunctionExpression(String expression, Global global) {
        super(expression, global);
        this.function = FunctionFactory.createFunction(expression, global);
    }

    public void applyContext(Context inputContext) {
        if (!isCached(inputContext)) {
            cache(inputContext, function.apply(Stream.of(inputContext)).toList());
        }
    }

    @Override
    public DataType type() {
        return DataType.FUNCTION;
    }
}
