package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.event.EventType;
import io.github.xmljim.json.parser.util.ByteSequence;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.StringJoiner;

class DataEvent extends AbstractJsonEvent {
    private EventType eventType;
    private ByteBuffer data;

    public DataEvent() {
    }

    public DataEvent(final int lineNumber, final int column, EventType eventType, ByteBuffer data) {
        super(lineNumber, column);
        this.data = data;
        this.eventType = eventType;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public ByteBuffer getData() {
        return data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataEvent.class.getSimpleName() + "[", "]")
            .add("eventType=" + eventType)
            .add("data=" + ByteSequence.withStartingSequence(data.array()))
            .add("line=" + super.getLineNumber())
            .add("column=" + super.getColumn())
            .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final DataEvent dataEvent)) {
            return false;
        }
        return eventType == dataEvent.eventType && Objects.equals(data, dataEvent.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, data, super.getColumn(), super.getLineNumber());
    }
}
