package io.github.xmljim.json.factory.parser.event;

import java.nio.ByteBuffer;

/**
 * A processing event produced by a {@link Processor}, handled by
 * an {@link EventHandler} and then subsequently assembed by an {@link Assembler}
 */
public interface JsonEvent {
    /**
     * The column number for the event
     *
     * @return the column number for the event
     */
    int getColumn();

    /**
     * The line number of the event
     *
     * @return the line number of the event
     */
    int getLineNumber();

    /**
     * The data associated with the event
     *
     * @return the data associated with the event
     */
    ByteBuffer getData();

    /**
     * The event type
     *
     * @return the event type
     */
    EventType getEventType();
}
