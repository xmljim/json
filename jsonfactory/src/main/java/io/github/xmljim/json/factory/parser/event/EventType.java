package io.github.xmljim.json.factory.parser.event;

public enum EventType {
    DOCUMENT_START,
    DOCUMENT_END,
    OBJECT_START,
    OBJECT_END,
    ARRAY_START,
    ARRAY_END,
    STRING_START,
    STRING_END,
    NUMBER_START,
    NUMBER_END,
    BOOLEAN_START,
    BOOLEAN_END,
    NULL_START,
    NULL_END,
    KEY_END,
    ENTITY_END
}
