package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.model.JsonNode;
import io.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class CountIfFunctionTest {

    @Test
    void testCountIfFunctionCompoundPredicate() {
        String expr = "$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') <= 2)]";
        try {
            JsonNode node = loadData("/compound-test.json");
            JsonPath jsonPath = ServiceManager.getProvider(JsonPathFactory.class).newJsonPath();
            System.out.println(jsonPath.select(node, expr).prettyPrint());
        } catch (Exception e) {
            fail(e);
        }
    }

    private JsonNode loadData(String classResource) throws Exception {
        try (InputData data = InputData.of(getClass().getResourceAsStream(classResource))) {
            ParserFactory parserFactory = ServiceManager.getProvider(ParserFactory.class);
            Parser parser = parserFactory.newParserBuilder().setUseStrict(false).build();
            return parser.parse(data);
        }
    }
}