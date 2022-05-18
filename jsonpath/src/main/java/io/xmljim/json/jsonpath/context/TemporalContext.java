package io.xmljim.json.jsonpath.context;

import java.time.temporal.Temporal;

class TemporalContext<T extends Temporal> extends SimpleValueContext {
    public TemporalContext(T value) {
        super(value);
    }
}
