package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.context.Context;

import java.util.function.Function;

public interface Argument<E, R> extends Function<Context, R> {
    String name();

    E element();
}
