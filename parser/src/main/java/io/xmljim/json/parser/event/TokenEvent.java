package io.xmljim.json.parser.event;

import io.xmljim.json.factory.parser.event.EventType;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.StringJoiner;

class TokenEvent extends AbstractJsonEvent {
    private final EventType eventType;

    public TokenEvent(final int lineNumber, final int column, EventType eventType) {
        super(lineNumber, column);
        this.eventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public ByteBuffer getData() {
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final TokenEvent that)) {
            return false;
        }
        return eventType == that.eventType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TokenEvent.class.getSimpleName() + "[", "]")
            .add("eventType=" + eventType)
            .add("lineNumber=" + super.getLineNumber())
            .add("column=" + super.getColumn())
            .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, super.getLineNumber(), super.getColumn());
    }
}
