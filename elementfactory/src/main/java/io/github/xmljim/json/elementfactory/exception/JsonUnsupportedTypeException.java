package io.github.xmljim.json.elementfactory.exception;

import io.github.xmljim.json.exception.JsonException;

public class JsonUnsupportedTypeException extends JsonException {
    public JsonUnsupportedTypeException() {
    }

    public JsonUnsupportedTypeException(String message) {
        super(message);
    }

    public JsonUnsupportedTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonUnsupportedTypeException(Throwable cause) {
        super(cause);
    }

    public JsonUnsupportedTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
