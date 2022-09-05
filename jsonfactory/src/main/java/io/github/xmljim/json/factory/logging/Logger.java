package io.github.xmljim.json.factory.logging;

public interface Logger<C extends Class<?>> {

    void trace(String message);

    void trace(Throwable throwable, String message);

    void trace(String format, Object... params);
}
