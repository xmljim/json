package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.PredicateArgument;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

abstract class AbstractPredicateFunction extends AbstractJsonPathFunction {
    public AbstractPredicateFunction(String name, List<Argument<?, ?>> arguments) {
        super(name, arguments);
    }

    PredicateArgument getPredicateArgument(String name) {
        return (PredicateArgument) getArgument(name).orElseThrow();
    }

    Predicate<Context> getPredicate(String name) {
        return getPredicateArgument(name).element();
    }

    public Stream<Context> testPredicate(String name, Context input) {
        Predicate<Context> predicate = getPredicate(name);

        if (input.type().isArray()) {
            return input.stream().filter(predicate);
        } else if (input.type().isObject()) {
            return predicate.test(input) ? Stream.of(input) : Stream.empty();
        }
        //TODO: revisit this
        return Stream.empty();
    }
}
