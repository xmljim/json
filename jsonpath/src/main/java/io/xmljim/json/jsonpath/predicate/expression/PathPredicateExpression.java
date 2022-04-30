package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;

import java.util.Optional;
import java.util.stream.Stream;

class PathPredicateExpression extends AbstractExpression {
    private final FilterStream sequence;
    private boolean executed = false;

    public PathPredicateExpression(String expression, Global global) {
        super(expression, global);
        sequence = Compiler.newPathCompiler(expression, true, global).compile();
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        if (!executed) { //only run once
            set(sequence.filter(Stream.of(inputContext)).toList());
            executed = true;
        }

        return index < size(inputContext) ? Optional.of(values().get(index)) : Optional.empty();
    }

    private void applyContext(Context inputContext) {
        if (!executed) { //only run once
            set(sequence.filter(Stream.of(inputContext)).toList());
            executed = true;
        }
    }

    @Override
    public int size(Context inputContext) {
        applyContext(inputContext);
        return values().size();
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.CONTEXT;
    }
}
