package io.xmljim.json.jsonpath;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.xmljim.json.factory.parser.InputData;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JsonPathAPITest extends JsonPathTestBase {

    @Test
    void testJsonPathFactory() {
        JsonPathFactory factory = getJsonPathFactory();
        assertNotNull(factory);
        assertTrue(factory instanceof JsonPathFactoryImpl);
    }

    @Test
    void testJsonPathBuilder() {
        JsonPathBuilder builder = getJsonPathBuilder();
        assertNotNull(builder);
    }

    @Test
    void testJsonPathInstanceDefault() {
        JsonPath jsonPath = getJsonPath();
        assertNotNull(jsonPath);
    }

    @Test
    void testSelectWithBooksInputData() {
        JsonPath jsonPath = getJsonPath();

        try (InputData data = InputData.of(getClass().getResourceAsStream("/books.json"))) {
            JsonArray array = jsonPath.select(data, "$..book[?(@.isbn)]");
            System.out.println(array.prettyPrint());
        } catch (Exception io) {
            fail(io);
        }
    }

    @Test
    void testSelectWithBooksJsonNode() throws Exception {
        JsonPath jsonPath = getJsonPath();
        JsonNode context = loadData("/books.json");
        JsonArray array = jsonPath.select(context, "$..book[?(@.isbn)]");
        System.out.println(array.prettyPrint());
    }

    @Test
    void testSelectWithBaseball() throws Exception {
        JsonPath jsonPath = getJsonPath();
        JsonNode context = loadData("/baseball.json");
        JsonArray array = jsonPath.select(context, "$.teams.*[?(@.id in $.divisions[0].teams)]");
        System.out.println(array.prettyPrint());
    }

    @Test
    void testCompoundPredicate() throws Exception {
        JsonPath jsonPath = getJsonPath();
        JsonNode context = loadData("/compound-test.json");
        long start = System.nanoTime();
        JsonArray array = jsonPath.select(context, "$.assignments.*[?(@.doc_type_id == 19 && @.doc_status != 'ASSIGNED')].count()");
        long end = System.nanoTime();
        System.out.println(array.prettyPrint());
        System.out.println(String.format("%.3f", ((double) (end - start) / 1_000_000_000)));
    }

    @Test
    void testCompoundPredicate2() throws Exception {
        JsonPath jsonPath = getJsonPath();
        JsonNode context = loadData("/crazy-doc.json");
        long start = System.nanoTime();
        JsonArray array = jsonPath.select(context, "$.PROGRAM_ASSIGNMENT_DATA.*[?(@.PA_DOC_ID == 19 && @.PA_STATUS == 'PROCESSED')].count()");
        long end = System.nanoTime();
        System.out.println(array.prettyPrint());
        System.out.println(String.format("%.3f", ((double) (end - start) / 1_000_000_000)));
    }

}
