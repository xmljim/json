package io.github.xmljim.json.factory.mapper.annotation;

/**
 * Specifies an argument for a ValueConverter
 */
public @interface ConverterArg {
    /**
     * The argument name
     *
     * @return the argument name
     */
    String name();

    /**
     * The argument value
     *
     * @return the argument value
     */
    String value();
}
