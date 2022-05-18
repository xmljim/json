package io.xmljim.json.jsonpathtest.function.predicate;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.jsonpathtest.JsonPathTestBase;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SumIfFunctionTest extends JsonPathTestBase {

    @Test
    void testSumIfWithIsbn() throws Exception {
        JsonNode sales = loadData("/store-sales.json");
        String expr = "$.stores.*[?(sum-if(@, @.region == 'Northeast', @.sales.*.items.*.lunch.total) > 75000.00)].id";
        //String expr = "$.stores.*[?(@.region == 'Northeast')].sales.*.items.*lunch.total.sum()";
        JsonPath jsonPath = getJsonPath();
        JsonArray result = jsonPath.select(sales, expr);
        assertNotNull(result);
        System.out.println(result.prettyPrint());
    }


}