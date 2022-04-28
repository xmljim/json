package io.xmljim.json.service;

import io.xmljim.json.exception.JsonException;

class VersionError extends JsonException {

    public VersionError() {
    }

    public VersionError(final String message) {
        super(message);
    }

    public VersionError(final String message, final Throwable cause) {
        super(message, cause);
    }

    public VersionError(final Throwable cause) {
        super(cause);
    }

    public VersionError(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
