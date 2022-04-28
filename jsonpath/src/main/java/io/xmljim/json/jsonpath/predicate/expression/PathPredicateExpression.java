package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;

import java.util.stream.Stream;

class PathPredicateExpression extends AbstractExpression {
    private final FilterStream sequence;

    public PathPredicateExpression(String expression, Global global) {
        super(expression, global);
        sequence = Compiler.newPathCompiler(expression, true, global).compile();
    }

    @Override
    public Context get(Context inputContext) {
        return sequence.filter(Stream.of(inputContext)).findFirst().orElse(Context.createSimpleContext(null));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Context inputContext) {
        return (T) get(inputContext).get().asJsonValue().get();
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.CONTEXT;
    }
}
