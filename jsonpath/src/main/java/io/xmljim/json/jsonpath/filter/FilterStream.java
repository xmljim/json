package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonNode;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public final class FilterStream extends LinkedList<Filter> {

    public Filter current() {
        return peek();
    }

    public Stream<Context> filter(Stream<Context> context) {
        AtomicReference<Stream<Context>> selected = new AtomicReference<>(context);
        this.forEach(filter -> selected.set(filter.apply(selected.get())));

        //TODO
        return selected.get();
    }

    public Stream<Context> filter(JsonNode jsonNode) {
        return filter(Stream.of(Context.create(jsonNode)));
    }

    private Stream<Context> runFilter(Stream<Context> input, Filter filter) {
        return this.current().apply(input);
    }
}
