package io.github.xmljim.json.factory.mapper;

import java.util.Map;
import java.util.Optional;

/**
 * Interface for value converters
 *
 * @param <T> the converter value type
 */
public interface ValueConverter<T> {

    /**
     * Specifies the class types this converter will accept
     *
     * @return the accepted class types
     */
    Class<?>[] accepts();

    /**
     * Contains a map of arguments/parameters for this converter
     *
     * @return a map of arguments/parameters for this converter
     */
    Map<String, String> args();

    /**
     * Convert the value
     *
     * @param value the input value
     * @param <V>   The output type
     * @return the output
     */
    <V> T convert(V value);

    static Optional<ValueConverter<?>> empty() {
        return Optional.empty();
    }
}
