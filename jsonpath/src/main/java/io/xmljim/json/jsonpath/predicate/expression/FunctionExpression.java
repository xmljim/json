package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.FunctionFactory;
import io.xmljim.json.jsonpath.function.JsonPathFunction;
import io.xmljim.json.jsonpath.variables.Global;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FunctionExpression extends AbstractExpression {
    private final JsonPathFunction function;

    public FunctionExpression(String expression, Global global) {
        super(expression, global);
        this.function = FunctionFactory.createFunction(expression, global);
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        List<Context> contexts = function.apply(Stream.of(inputContext)).toList();

        if (index <= contexts.size() - 1) {
            return Optional.of(contexts.get(0));
        }

        return Optional.empty();
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.FUNCTION;
    }
}
