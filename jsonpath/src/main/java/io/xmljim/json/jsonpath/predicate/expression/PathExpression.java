package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Filter;

import java.util.stream.Stream;

public interface PathExpression {

    /**
     * Subfilters evaluate the current expression and determine if the
     * last filter in the stream is a child filter, and removes it
     * to execute a subfilter on the context element
     *
     * @param inputContext the context to apply the subfilter to
     * @return the results of the subfilter
     */
    Stream<Context> subfilter(Context inputContext);

    Filter getLastFilter();
}
