package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.context.Context;

import java.util.stream.Stream;

public interface Filter {
    String getExpression();

    FilterType getOperatorType();

    Stream<Context> apply(Stream<Context> inputs);

    Stream<Context> apply(Context input);


}
