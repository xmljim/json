package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.variables.Global;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;

import java.util.function.Predicate;
import java.util.stream.Stream;

class PredicateFilter extends AbstractFilter {
    private final Predicate<Context> predicate;

    public PredicateFilter(String expression, Global global) {
        super(expression, FilterType.PREDICATE, global);
        this.predicate = Compiler.newPredicateCompiler(expression, getGlobal()).compile();
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        if (input.type().isArray()) {
            return input.stream().filter(predicate);
        } else if (input.type().isObject()) {
            return predicate.test(input) ? Stream.of(input) : Stream.empty();
        }

        return Stream.empty();


    }
}
