package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.context.Context;

import java.util.stream.Stream;

class NoOpOperator extends AbstractFilter {
    public NoOpOperator(String expression) {
        super(expression, FilterType.INVALID, null);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return null;
    }

    @Override
    public Stream<Context> apply(Context input) {
        return null;
    }
}
