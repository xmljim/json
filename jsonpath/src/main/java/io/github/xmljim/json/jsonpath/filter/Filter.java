package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.context.Context;

import java.util.stream.Stream;

public interface Filter {
    String getExpression();

    FilterType getFilterType();

    Stream<Context> apply(Stream<Context> inputs);

    Stream<Context> apply(Context input);


}
