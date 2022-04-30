package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Filter;

import java.util.stream.Stream;

public interface PathExpression {

    Stream<Context> subfilter(Context inputContext);

    Filter getLastFilter();
}
