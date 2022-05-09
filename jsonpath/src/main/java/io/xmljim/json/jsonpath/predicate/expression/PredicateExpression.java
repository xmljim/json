package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.filter.FilterType;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class PredicateExpression extends CachedExpression implements PathExpression {
    private final FilterStream sequence;
    private boolean executed = false;
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public PredicateExpression(String expression, Global global) {
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
    public ExpressionType type() {
        return ExpressionType.CONTEXT;
    }


    @Override
    public Stream<Context> subfilter(Context inputContext) {
        //Since predicates are typically run over multiple items
        //we need to clone the stream before removing the child
        //filter. In that way we're not permanently removing the filter
        FilterStream clonedSequence = (FilterStream) sequence.clone();

        assert clonedSequence.peekLast() != null;
        if (clonedSequence.size() > 1 && clonedSequence.peekLast().getOperatorType() == FilterType.CHILD) {
            assert clonedSequence.peekLast() != null;
            clonedSequence.removeLast();
        }

        return clonedSequence.filter(Stream.of(inputContext));
    }

    public Filter getLastFilter() {
        return sequence.peekLast();
    }
}
