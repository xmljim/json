package io.github.xmljim.json.factory.jsonpath;

import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonNode;

import java.util.List;

/**
 * JSONPath interface for querying elements in a JSON node
 */
public interface JsonPath {

    /**
     * query a JsonNode using a JsonPath expression
     *
     * @param jsonNode   the JsonNode instance
     * @param expression the JsonPath expression
     * @return a JsonArray containing the results of the query
     */
    default JsonArray select(JsonNode jsonNode, String expression) {
        return select(jsonNode, expression, ResultType.VALUE);
    }

    /**
     * Load a Json instance, then query using a JsonPath expression
     *
     * @param inputData  the InputData instance referencing the Json data
     * @param expression the JsonPath expression
     * @return a JsonArray containing the results of the query
     */
    default JsonArray select(InputData inputData, String expression) {
        return select(inputData, expression, ResultType.VALUE);
    }

    /**
     * Query a JsonNode instance and return results from a JsonPath expression
     *
     * @param jsonNode   the JsonNode instance
     * @param expression the JsonPath expression
     * @param resultType specifies the result type
     * @return if {@link ResultType#PATH} is specified, it will return normalized paths for each result;
     * if {@link ResultType#VALUE} is specified, the actual values will be returned
     */
    JsonArray select(JsonNode jsonNode, String expression, ResultType resultType);

    /**
     * Load a JSON instance and return results from a JsonPath expression
     *
     * @param inputData  the input
     * @param expression the JsonPath expression
     * @param resultType specifies the result type
     * @return if {@link ResultType#PATH} is specified, it will return normalized paths for each result;
     * if {@link ResultType#VALUE} is specified, the actual values will be returned
     */
    JsonArray select(InputData inputData, String expression, ResultType resultType);

    /**
     * Return a single value from a JsonPath expression
     *
     * @param jsonNode   the JsonNode instance
     * @param expression the JsonPath expression
     * @param <T>        the value type
     * @return the value
     */
    @SuppressWarnings("unchecked")
    default <T> T selectValue(JsonNode jsonNode, String expression) {
        JsonArray array = select(jsonNode, expression);

        if (array.size() == 1) {
            return (T) array.get(0);
        }

        return null;
    }

    List<JsonPathError> getErrors();
}
