package io.xmljim.json.mapper.exception;

public class JsonMapperConfigurationException extends JsonMapperException {
    
    public JsonMapperConfigurationException() {
    }

    public JsonMapperConfigurationException(String message) {
        super(message);
    }

    public JsonMapperConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonMapperConfigurationException(Throwable cause) {
        super(cause);
    }

    public JsonMapperConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
