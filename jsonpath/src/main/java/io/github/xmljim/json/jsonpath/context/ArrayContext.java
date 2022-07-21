package io.github.xmljim.json.jsonpath.context;

import io.github.xmljim.json.model.JsonArray;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class ArrayContext extends Context {
    private final JsonArray thisArray;

    protected ArrayContext(JsonArray root) {
        super(root);
        thisArray = root;
    }

    protected ArrayContext(JsonArray element, Context parent, Accessor accessor) {
        super(element, parent, accessor);
        thisArray = element;
    }

    @Override
    public boolean canTraverse() {
        return childLength() > 0;
    }

    @Override
    public Stream<Context> stream() {
        return IntStream.range(0, childLength()).mapToObj(i -> Context.create(thisArray.getValue(i), this, Accessor.of(i)));
    }

    @Override
    public int childLength() {
        return thisArray.size();
    }
}
