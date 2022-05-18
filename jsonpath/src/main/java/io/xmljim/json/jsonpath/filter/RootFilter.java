package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.util.Global;

import java.util.stream.Stream;

class RootFilter extends AbstractFilter {
    public RootFilter(Global global) {
        super("$", FilterType.ROOT, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        return Stream.of(input.root());
    }
}
