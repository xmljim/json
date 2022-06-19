package io.github.xmljim.json.factory.parser.event;

/**
 * Enumeration of event types
 */
public enum EventType {
    /**
     * Document Start - either with a <code>{</code> or {@code [}
     */
    DOCUMENT_START,
    /**
     * Document End
     */
    DOCUMENT_END,
    /**
     * JsonObject start - with a <code>{</code>
     */
    OBJECT_START,
    /**
     * JsonObject end
     */
    OBJECT_END,
    /**
     * JsonArray start
     */
    ARRAY_START,
    /**
     * JsonArray end
     */
    ARRAY_END,
    /**
     * String value start
     */
    STRING_START,
    /**
     * String value end
     */
    STRING_END,
    /**
     * Number value start
     */
    NUMBER_START,
    /**
     * Number value end
     */
    NUMBER_END,
    /**
     * Boolean value start
     */
    BOOLEAN_START,

    /**
     * Boolean value end
     */
    BOOLEAN_END,
    /**
     * Null value start
     */
    NULL_START,
    /**
     * Null value end
     */
    NULL_END,

    /**
     * JsonObject key start
     */
    KEY_END,

    /**
     * JsonObject key end
     */
    ENTITY_END
}
