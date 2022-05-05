package io.xmljim.json.factory.jsonpath;

import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonNode;

public interface JsonPath {

    default JsonArray select(JsonNode jsonNode, String expression) {
        return select(jsonNode, expression, ResultType.VALUE);
    }

    ;

    default JsonArray select(InputData inputData, String expression) {
        return select(inputData, expression, ResultType.VALUE);
    }

    JsonArray select(JsonNode jsonNode, String expression, ResultType resultType);

    JsonArray select(InputData inputData, String expression, ResultType resultType);
}
