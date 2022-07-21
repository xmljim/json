package io.github.xmljim.json.factory.mapper.parser;

import io.github.xmljim.json.factory.mapper.Mapper;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;

/**
 * Wrapper interface around a {@link Parser} and {@link Mapper}.
 * It's a parser <em>in name only</em> in the sense that it
 * uses a Parser to load the {@link io.github.xmljim.json.model.JsonObject}
 * data. After parsing, the JsonObject instance is passed to a Mapper to
 * create a class instance.
 * <p>
 * Internally this is a functional interface that passes in
 * an {@link InputData} and {@code Class} targetClass
 * </p>
 */
@FunctionalInterface
public interface MappingParser {

    /**
     * Parse a Json data and load into a class instance
     *
     * @param inputData   The input data
     * @param targetClass the target class
     * @param <T>
     * @return
     */
    <T> T parse(InputData inputData, Class<T> targetClass);
}
