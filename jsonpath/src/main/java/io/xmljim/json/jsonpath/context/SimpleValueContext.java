package io.xmljim.json.jsonpath.context;

import java.util.stream.Stream;

class SimpleValueContext extends Context {
    public <T> SimpleValueContext(T value) {
        super(value);
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
