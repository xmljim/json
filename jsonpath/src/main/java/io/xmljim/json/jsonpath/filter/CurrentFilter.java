package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

import java.util.stream.Stream;

class CurrentFilter extends AbstractFilter {
    public CurrentFilter(Global global) {
        super("@", FilterType.CURRENT, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> inputs) {
        return inputs.flatMap(this::apply);
    }

    @Override
    public Stream<Context> apply(Context input) {
        return Stream.of(input);
    }
}
