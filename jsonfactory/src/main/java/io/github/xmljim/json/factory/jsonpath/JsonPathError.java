package io.github.xmljim.json.factory.jsonpath;

public interface JsonPathError {
    String getPath();

    String getErrorMessage();

    Throwable getThrowable();
}
