package io.xmljim.json.factory.jsonpath;

import io.xmljim.json.service.JsonService;

public interface JsonPathFactory extends JsonService {

    JsonPathBuilder newJsonPathBuilder();

    default JsonPath newJsonPath() {
        return newJsonPathBuilder().build();
    }
}
