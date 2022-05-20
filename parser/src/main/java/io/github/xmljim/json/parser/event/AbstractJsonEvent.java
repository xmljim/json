package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.event.JsonEvent;

import java.util.StringJoiner;

abstract class AbstractJsonEvent implements JsonEvent {
    private int lineNumber;
    private int column;

    protected AbstractJsonEvent() {
        //no-op constructor
    }

    protected AbstractJsonEvent(int lineNumber, int column) {
        this.lineNumber = lineNumber;
        this.column = column;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractJsonEvent.class.getSimpleName() + "[", "]")
            .add("lineNumber=" + lineNumber)
            .add("column=" + column)
            .toString();
    }
}
