package io.github.xmljim.json.service.exception;

public class JsonServiceProviderUnavailableException extends JsonPluginException {
    public JsonServiceProviderUnavailableException() {
        super();
    }

    public JsonServiceProviderUnavailableException(String message) {
        super(message);
    }

    public JsonServiceProviderUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonServiceProviderUnavailableException(Throwable cause) {
        super(cause);
    }

    protected JsonServiceProviderUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
