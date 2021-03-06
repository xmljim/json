package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.function.FunctionFactory;
import io.github.xmljim.json.jsonpath.function.JsonPathFunction;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.stream.Stream;

class FunctionFilter extends AbstractFilter {
    private final JsonPathFunction function;

    public FunctionFilter(String expression, Global global) {
        super(expression, FilterType.FUNCTION, global);
        this.function = FunctionFactory.createFunction(expression, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return function.apply(inputs);
    }

    @Override
    public Stream<Context> apply(Context input) {
        return null;
    }
}
