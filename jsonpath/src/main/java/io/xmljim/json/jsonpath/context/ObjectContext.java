package io.xmljim.json.jsonpath.context;

import io.xmljim.json.jsonpath.filter.Accessor;
import io.xmljim.json.model.JsonObject;

import java.util.stream.Stream;

class ObjectContext extends Context {
    private final JsonObject thisObject;

    protected ObjectContext(JsonObject root) {
        super(root);
        thisObject = root;
    }

    protected ObjectContext(JsonObject element, Context parent, Accessor accessor) {
        super(element, parent, accessor);
        thisObject = element;
    }

    @Override
    public boolean canTraverse() {
        return thisObject.size() > 0;
    }

    @Override
    public Stream<Context> stream() {
        return thisObject.keys().map(key -> Context.create(thisObject.getValue(key), this, Accessor.of(key)));
    }

    @Override
    public int childLength() {
        return thisObject.size();
    }
}
