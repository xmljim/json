package io.github.xmljim.json.mapper.exception;

import io.github.xmljim.json.exception.JsonException;

public class JsonMapperException extends JsonException {
    public JsonMapperException() {
    }

    public JsonMapperException(String message) {
        super(message);
    }

    public JsonMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonMapperException(Throwable cause) {
        super(cause);
    }

    public JsonMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
