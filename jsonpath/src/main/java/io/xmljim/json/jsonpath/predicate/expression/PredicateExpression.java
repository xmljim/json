package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.filter.FilterType;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class PredicateExpression extends AbstractExpression implements PathExpression {
    private final FilterStream sequence;
    private boolean executed = false;
    private final ConcurrentHashMap<String, List<Context>> concurrentHashMap = new ConcurrentHashMap<>();

    public PredicateExpression(String expression, Global global) {
        super(expression, global);
        sequence = Compiler.newPathCompiler(expression, true, global).compile();
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        applyContext(inputContext);
        return index < size(inputContext) ? Optional.of(values(inputContext).get(index)) : Optional.empty();
    }

    private void applyContext(Context inputContext) {
        if (!isCached(inputContext)) {
            cache(inputContext, sequence.filter(Stream.of(inputContext)).toList());
            executed = true;
        }
    }

    private void cache(Context inputContext, List<Context> values) {
        String key = getExpression() + "_" + inputContext.toString();
        concurrentHashMap.putIfAbsent(key, values);
        System.out.println("Cached [" + key + "]: " + values);
    }

    private boolean isCached(Context inputContext) {
        String key = getExpression() + "_" + inputContext.toString();
        return concurrentHashMap.containsKey(key);
    }

    public List<Context> values(Context inputContext) {
        String key = getExpression() + "_" + inputContext.toString();
        //System.out.println("Get Cache [" + key + "]");
        return concurrentHashMap.getOrDefault(key, Collections.emptyList());
    }

    @Override
    public int size(Context inputContext) {
        //System.out.println("size [" + getExpression() + "]: " + inputContext);
        applyContext(inputContext);
        return values(inputContext).size();
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
