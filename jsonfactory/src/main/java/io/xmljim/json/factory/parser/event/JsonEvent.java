package io.xmljim.json.factory.parser.event;

import java.nio.ByteBuffer;

public interface JsonEvent {
    int getColumn();

    int getLineNumber();

    ByteBuffer getData();

    EventType getEventType();
}
