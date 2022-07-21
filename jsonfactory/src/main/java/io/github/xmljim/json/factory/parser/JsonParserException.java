package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.exception.JsonException;

/**
 * A parser exception
 */
public class JsonParserException extends JsonException {
    /**
     * Constructor
     */
    public JsonParserException() {
        super();
    }

    /**
     * Constructor
     *
     * @param message message
     */
    public JsonParserException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param cause the underlying exception
     */
    public JsonParserException(Throwable cause) {
        super(cause);
    }

}
