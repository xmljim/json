package io.github.xmljim.json.factory.serializer;

public interface SerializationEvent {
    SerializationEventType getEventType();

    SerializationContext getContext();

    <T> T getValue();
}
