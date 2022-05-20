package io.github.xmljim.json.service.exception;

import io.github.xmljim.json.exception.JsonException;

public class JsonPluginException extends JsonException {
    public JsonPluginException() {
        super();
    }

    public JsonPluginException(String message) {
        super(message);
    }

    public JsonPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPluginException(Throwable cause) {
        super(cause);
    }

    protected JsonPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
