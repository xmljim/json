package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class RecursionFilter extends AbstractFilter {

    private final List<Context> cache = new ArrayList<>();


    public RecursionFilter(String expression, Global global) {
        super(expression, FilterType.RECURSIVE, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        if (input.canTraverse()) {
            recurse(input.stream());
            return cache.stream();
        }

        return Stream.empty();
    }

    private void recurse(Stream<Context> data) {
        data.forEach(context -> {
            cache.add(context);
            if (context.canTraverse()) {
                recurse(context.stream());
            }
        });
    }
}
