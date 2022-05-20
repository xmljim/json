package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.compiler.Compiler;
import io.github.xmljim.json.jsonpath.filter.Filter;
import io.github.xmljim.json.jsonpath.filter.FilterStream;
import io.github.xmljim.json.jsonpath.filter.FilterType;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class PathExpression extends CachedExpression implements FilterExpression {
    private final FilterStream sequence;
    private boolean executed = false;
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public PathExpression(String expression, Global global) {
        super(expression, global);
        sequence = Compiler.newPathCompiler(expression, true, global).compile();
    }

    public void applyContext(Context inputContext) {
        if (!isCached(inputContext)) {
            cache(inputContext, sequence.filter(Stream.of(inputContext)).toList());
            executed = true;
        }
    }

    @Override
    public DataType type() {
        return DataType.CONTEXT;
    }


    @Override
    public Stream<Context> subfilter(Context inputContext) {
        //Since predicates are typically run over multiple items
        //we need to clone the stream before removing the child
        //filter. In that way we're not permanently removing the filter
        FilterStream clonedSequence = (FilterStream) sequence.clone();

        assert clonedSequence.peekLast() != null;
        if (clonedSequence.size() > 1 && clonedSequence.peekLast().getFilterType() == FilterType.CHILD) {
            assert clonedSequence.peekLast() != null;
            clonedSequence.removeLast();
        }

        return clonedSequence.filter(Stream.of(inputContext));
    }

    public Filter getLastFilter() {
        return sequence.peekLast();
    }
}
