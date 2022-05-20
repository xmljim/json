package io.github.xmljim.json.jsonpath.filter;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.stream.Stream;

class ChildFilter extends AbstractFilter {
    private final Accessor accessor;

    public ChildFilter(String expression, Global global) {
        super(expression, FilterType.CHILD, global);
        this.accessor = Accessor.parse(expression);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        if (input.canTraverse()) {
            if (input.get().type().isArray()) {
                if (accessor.isString()) {
                    return Stream.empty();
                }

                int access = accessor.get();
                if (access < 0) {
                    access = access + input.get().asJsonArray().size();
                }

                if ((access >= 0) && (input.get().asJsonArray().size() >= access + 1)) {
                    return Stream.of(Context.create(input.get().asJsonArray().getValue(access), input, accessor));
                } else {
                    return Stream.empty(); //invalid index
                }
            } else {
                String access = accessor.get();
                return input.get().asJsonObject().containsKey(access) ?
                    Stream.of(Context.create(input.get().asJsonObject().getValue(access), input, accessor)) :
                    Stream.empty();
            }
        }

        return Stream.empty();
    }
}
