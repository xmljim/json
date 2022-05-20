package io.github.xmljim.json.jsonpath;

import io.github.xmljim.json.exception.JsonException;

public class JsonPathException extends JsonException {
    public JsonPathException() {
    }

    public JsonPathException(String message) {
        super(message);
    }

    public JsonPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPathException(Throwable cause) {
        super(cause);
    }

    public JsonPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
