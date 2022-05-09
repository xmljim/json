package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.jsonpath.JsonPathTestBase;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.model.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CountIfFunctionTest extends JsonPathTestBase {

    @Test
    void testCountIfFunctionCompoundPredicateWithGreaterThanOperator() {
        String expr = "$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') > 2)]";
        try {
            JsonNode node = loadData("/compound-test.json");
            JsonPath jsonPath = getJsonPath();
            JsonArray results = jsonPath.select(node, expr);
            assertEquals(1, results.size());
            JsonObject result = results.get(0);
            long id = result.get("id");
            assertEquals(2, id);
            assertTrue(getExpressionValue(id) > 2);
            System.out.println(jsonPath.select(node, expr).prettyPrint());
        } catch (Exception e) {
            fail(e);
        }
    }


    @Test
    void testCountIfFunctionCompoundPredicateWithLessThanEqualOperator() {
        String expr = "$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') <= 2)]";
        try {
            JsonNode node = loadData("/compound-test.json");
            JsonPath jsonPath = getJsonPath();
            JsonArray results = jsonPath.select(node, expr);
            assertEquals(1, results.size());
            JsonObject result = results.get(0);
            long id = result.get("id");
            assertEquals(1, id);
            assertTrue(getExpressionValue(id) <= 2);
            System.out.println(jsonPath.select(node, expr).prettyPrint());
        } catch (Exception e) {
            fail(e);
        }
    }

    private long getExpressionValue(long id) {
        String expr = "$.*[?(@.id == " + id + ")].assignments.*[?(@.doc_type_id == 19 && @.doc_status == 'PROCESSED')].count()";
        try {
            JsonArray array = getJsonPath().select(loadData("/compound-test.json"), expr);
            System.out.println(array.toJsonString());
            return array.get(0);
        } catch (Exception e) {
            fail(e);
        }
        return -1;
    }

}