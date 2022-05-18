package io.xmljim.json.factory.jsonpath;

import io.xmljim.json.service.JsonService;

/**
 * Factory service for creating a new JsonPath instance
 */
public interface JsonPathFactory extends JsonService {

    /**
     * Return a new JsonPathBuilder to configure the JsonPath instance
     *
     * @return the JsonPathBuilder to configure the JsonPath instance
     */
    JsonPathBuilder newJsonPathBuilder();

    /**
     * Return a new JsonPath instance with a default configuration
     *
     * @return a new JsonPath instance
     */
    default JsonPath newJsonPath() {
        return newJsonPathBuilder().build();
    }
}
