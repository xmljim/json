package io.github.xmljim.json.factory.jsonpath;

/**
 * Builder for creating a {@link JsonPath} instance with the appropriate settings
 */
public interface JsonPathBuilder {

    /**
     * Build and return a {@link JsonPath} instance
     *
     * @return a new JsonPath instance
     */
    JsonPath build();

    /**
     * Set a variable that can be used by a JsonPath expression
     *
     * @param name  the variable name
     * @param value the variable value, the value can be a primitive or a JsonNode instance
     * @param <T>   The data type for the value
     * @return The current JsonBuilder
     */
    <T> JsonPathBuilder setVariable(String name, T value);

    /**
     * Sets a property that can be used by the JsonPath implementation.
     *
     * <p>
     *     <div><strong>NOTE:</strong></div>
     *     <div>Properties are implementation-specific. Consult the implementation for
     *      recognized properties and values
     *     </div>
     * </p>
     *
     * @param name  The property name
     * @param value The property value
     * @param <T>   the value type for the property
     * @return The JsonPathBuilder instance
     */
    <T> JsonPathBuilder setProperty(String name, T value);
}
