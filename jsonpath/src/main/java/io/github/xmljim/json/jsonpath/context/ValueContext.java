package io.github.xmljim.json.jsonpath.context;

import io.github.xmljim.json.jsonpath.filter.Accessor;
import io.github.xmljim.json.model.JsonValue;

import java.util.stream.Stream;

class ValueContext extends Context {
    private final JsonValue<?> thisValue;


    protected ValueContext(JsonValue<?> element, Context parentContext, Accessor accessor) {
        super(element, parentContext, accessor);
        thisValue = element;
    }

    @Override
    public boolean canTraverse() {
        return false;
    }

    @Override
    public Stream<Context> stream() {
        return Stream.empty();
    }

    @Override
    public int childLength() {
        return 0;
    }
}
