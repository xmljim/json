package io.xmljim.json.jsonpath.function;


import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractJsonPathFunction implements JsonPathFunction {
    private final String name;
    private final List<Argument<?, ?>> args;

    private final Global global;


    public AbstractJsonPathFunction(String name, List<Argument<?, ?>> arguments, Global global) {
        this.name = name;
        this.args = arguments;
        this.global = global;
    }

    public AbstractJsonPathFunction(BuiltIns builtIn, List<Argument<?, ?>> arguments, Global global) {
        this(builtIn.functionName(), arguments, global);
    }

    public String name() {
        return name;
    }

    @Override
    public List<Argument<?, ?>> arguments() {
        return args;
    }

    @Override
    public Global getGlobal() {
        return global;
    }

    public <T> T getArgumentValue(String name, Context context, int index) {
        Argument<?, ?> arg = (Argument<?, ?>) getArgument(name, index)
                .orElseThrow(() -> new JsonPathException("No argument found with name: " + name));
        return getArgumentValue(arg, context);
    }

    @SuppressWarnings("unchecked")
    private <T> T getArgumentValue(Argument<?, ?> arg, Context context) {
        if (arg instanceof ExpressionArgument expressionArgument) {
            return ((ExpressionArgument) arg).apply(context).value();
        } else {
            return (T) ((PredicateArgument) arg).apply(context);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getArgElement(String name, int index) {
        Optional<Argument<?, ?>> arg = getArgument(name, index);
        return arg.flatMap(argument -> (Optional<T>) Optional.of(argument.element()));
    }

    public <T> Optional<T> getArgElement(String name) {
        return getArgElement(name, 0);
    }

    public Expression getExpression(String name, int index) {
        return (Expression) getArgElement(name, index)
                .orElseThrow(() -> new JsonPathException("No argument found for '" + name + "' at index " + index));

    }

    public Expression getExpression(String name) {
        return getExpression(name, 0);
    }

    public <T> T getArgumentValue(String name, Context context) {
        return getArgumentValue(name, context, 0);
    }

    public <T> Stream<T> getArgumentValueStream(String name, Context context) {
        return arguments().stream().filter(argument -> argument.name().equals(name))
                .map(argument -> getArgumentValue(argument, context));
    }
}
