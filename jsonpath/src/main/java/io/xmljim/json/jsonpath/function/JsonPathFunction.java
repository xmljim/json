package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.List;
import java.util.function.Function;

public interface JsonPathFunction<T, R> extends Function<T, R> {
    List<PredicateExpression> args();

    String name();

}
