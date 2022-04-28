package io.xmljim.json.factory.parser;

import io.xmljim.json.exception.JsonException;

public class JsonParserException extends JsonException {
    public JsonParserException() {
        super();
    }

    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParserException(Throwable cause) {
        super(cause);
    }

    protected JsonParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
