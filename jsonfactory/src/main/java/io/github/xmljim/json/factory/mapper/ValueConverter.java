package io.github.xmljim.json.factory.mapper;

import java.util.Map;
import java.util.Optional;

public interface ValueConverter<T> {

    Class<?>[] accepts();

    Map<String, String> args();

    <V> T convert(V value);

    static Optional<ValueConverter<?>> empty() {
        return Optional.empty();
    }
}
