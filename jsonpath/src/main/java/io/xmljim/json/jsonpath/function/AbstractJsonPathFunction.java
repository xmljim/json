package io.xmljim.json.jsonpath.function;


import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.context.Context;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractJsonPathFunction implements JsonPathFunction {
    private final String name;
    private final List<Argument<?, ?>> args;


    public AbstractJsonPathFunction(String name, List<Argument<?, ?>> arguments) {
        this.name = name;
        this.args = arguments;
    }

    public String name() {
        return name;
    }

    @Override
    public List<Argument<?, ?>> arguments() {
        return args;
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

    public <T> T getArgumentValue(String name, Context context) {
        return getArgumentValue(name, context, 0);
    }

    public <T> Stream<T> getArgumentValueStream(String name, Context context) {
        return arguments().stream().filter(argument -> argument.name().equals(name))
            .map(argument -> getArgumentValue(argument, context));
    }
}
