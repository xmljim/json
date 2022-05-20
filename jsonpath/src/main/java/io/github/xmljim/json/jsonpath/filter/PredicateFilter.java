package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.compiler.Compiler;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.Global;

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
