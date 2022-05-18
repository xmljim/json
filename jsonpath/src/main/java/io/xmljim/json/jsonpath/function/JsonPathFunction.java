package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface JsonPathFunction extends Function<Stream<Context>, Stream<Context>> {

    List<Argument<?, ?>> arguments();

    String name();

    Global getGlobal();

    @SuppressWarnings("unchecked")
    default <T, E, R extends Argument<E, R>> Optional<T> getArgument(String argName) {
        return (Optional<T>) getArgument(argName, 0);
    }

    default List<Argument<?, ?>> getArguments(String name) {
        return arguments().stream().filter(argument -> argument.name().equals(name)).toList();
    }

    default Optional<Argument<?, ?>> getArgument(String name, int index) {
        List<Argument<?, ?>> args = getArguments(name);

        if (index <= args.size() - 1) {
            return Optional.of(args.get(index));
        }

        return Optional.empty();
    }

    default boolean hasArgument(String name) {
        return arguments().stream().anyMatch(argument -> argument.name().equals(name));
    }
}
