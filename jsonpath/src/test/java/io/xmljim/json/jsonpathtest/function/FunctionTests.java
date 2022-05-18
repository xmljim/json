package io.xmljim.json.jsonpathtest.function;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.jsonpathtest.JsonPathTestBase;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonElement;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.model.JsonValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Built-in Function Tests")
class FunctionTests extends JsonPathTestBase {

    @Test
    @DisplayName("average function test: Books")
    void testAverageFunctionBooks() {
        try {
            JsonObject object = getBooks();
            JsonObject store = object.get("store");
            JsonArray books = store.get("book");
            double avg = books.jsonValues().mapToDouble(v -> (double) v.asJsonObject().value("price").map(JsonValue::value).orElse(0.0)).average().orElse(0.0);

            JsonArray array = selectForTest("/books.json", "$..book.*.price.average()");

            assertEquals(avg, array.get(0));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("concat function test: Books")
    void testConcatFunctionBooks() {
        try {
            JsonArray array = selectForTest("/books.json", "$..book.*.title.concat(@, '-test')");
            array.jsonValues().forEach(value -> {
                assertTrue(value.asString().endsWith("-test"));
            });
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("count function test: Books")
    void testCountFunctionBooks() {
        try {
            JsonArray array = selectForTest("/books.json", "$..book.*.count()");
            JsonObject object = getBooks();
            JsonObject store = object.get("store");
            JsonArray books = store.get("book");

            assertEquals(books.size(), array.getValue(0).asNumber().intValue());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("count-if test: assignments >= 3")
    void testCountIfFunctionCompoundTest_GreaterThanThree() {
        String expr = "$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') >= 3)]";
        try {
            JsonArray array = selectForTest("/compound-test.json", expr);
            //now the ugly way:
            JsonArray compoundArray = getCompoundTest();
            List<JsonObject> result = compoundArray.jsonValues().map(JsonElement::asJsonObject)
                .filter(jsonObject -> jsonObject.getValue("assignments").asJsonArray().jsonValues()
                    .filter(assignment -> {
                        JsonObject assigned = assignment.asJsonObject();
                        return (assigned.getValue("doc_type_id").asNumber().intValue() == 19 &&
                            assigned.getValue("doc_status").asString().equals("PROCESSED"));
                    }).count() >= 3)
                .toList();

            assertEquals(array.size(), result.size());
            assertEquals(result.get(0).value("id").orElseThrow(() -> new Exception("Bad Expected Value")),
                array.getValue(0).asJsonObject().value("id").orElseThrow());

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("count-if test: assignments < 3")
    void testCountIfFunctionCompoundTest_LessThanThree() {
        String expr = "$.*[?(count-if(@.assignments.*, @.doc_type_id == 19 && @.doc_status == 'PROCESSED') < 3)]";
        try {
            JsonArray array = selectForTest("/compound-test.json", expr);
            //now the ugly way:
            JsonArray compoundArray = getCompoundTest();
            List<JsonObject> result = compoundArray.jsonValues().map(JsonElement::asJsonObject)
                .filter(jsonObject -> jsonObject.getValue("assignments").asJsonArray().jsonValues()
                    .filter(assignment -> {
                        JsonObject assigned = assignment.asJsonObject();
                        return (assigned.getValue("doc_type_id").asNumber().intValue() == 19 &&
                            assigned.getValue("doc_status").asString().equals("PROCESSED"));
                    }).count() < 3)
                .toList();

            assertEquals(array.size(), result.size());
            assertEquals(result.get(0).value("id").orElseThrow(() -> new Exception("Bad Expected Value")),
                array.getValue(0).asJsonObject().value("id").orElseThrow());

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("distinct-values(): boolean")
    void testDistinctValuesBoolean() {
        String expr = "$.boolean.*.distinct-values()";
        try {
            JsonArray array = selectForTest("/distinct-values.json", expr);

            //The other way
            JsonObject distinctObject = getDistinctValues();
            JsonArray booleanValues = distinctObject.get("boolean");
            @SuppressWarnings("unchecked")
            List<Boolean> results = (List<Boolean>) booleanValues.values().distinct().toList();

            assertEquals(results.size(), array.size());
            assertEquals(2, results.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("distinct-values(): string")
    void testDistinctValuesString() {
        String expr = "$.string.*.distinct-values()";
        try {
            JsonArray array = selectForTest("/distinct-values.json", expr);

            //The other way
            JsonObject distinctObject = getDistinctValues();
            JsonArray stringValues = distinctObject.get("string");
            @SuppressWarnings("unchecked")
            List<Boolean> results = (List<Boolean>) stringValues.values().distinct().toList();

            assertEquals(results.size(), array.size());
            assertEquals(4, results.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("distinct-values(): number")
    void testDistinctValuesNumber() {
        String expr = "$.number.*.distinct-values()";
        try {
            JsonArray array = selectForTest("/distinct-values.json", expr);

            //The other way
            JsonObject distinctObject = getDistinctValues();
            JsonArray numberValues = distinctObject.get("number");
            @SuppressWarnings("unchecked")
            List<Boolean> results = (List<Boolean>) numberValues.values().distinct().toList();

            assertEquals(results.size(), array.size());
            assertEquals(3, array.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("distinct-values(): array")
    void testDistinctValuesArray() {
        String expr = "$.array.*.distinct-values()";
        try {
            JsonArray array = selectForTest("/distinct-values.json", expr);

            //The other way
            JsonObject distinctObject = getDistinctValues();
            JsonArray arrayValues = distinctObject.get("array");
            @SuppressWarnings("unchecked")
            List<Boolean> results = (List<Boolean>) arrayValues.values().distinct().toList();

            assertEquals(results.size(), array.size());
            //assertEquals(6, array.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("distinct-values(): objects")
    void testDistinctValuesObject() {
        String expr = "$.object.*.distinct-values()";
        try {
            //note that the values in distinct-values.json are intentionally mixed around so that
            //keys are not in the same sequence, though the sets of three have identical key-value pairs
            JsonArray array = selectForTest("/distinct-values.json", expr);

            //The other way
            JsonObject distinctObject = getDistinctValues();
            JsonArray objectValues = distinctObject.get("object");
            List<JsonObject> results = objectValues.jsonValues().map(JsonElement::asJsonObject).distinct().toList();
            System.out.println(array.prettyPrint());
            assertEquals(results.size(), array.size());
            assertEquals(3, array.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("ends-with(): baseball")
    void testEndsWithFunction() {
        try {
            JsonObject baseball = getBaseballData();
            JsonPath jsonPath = getJsonPath();


            //Boston: Fenway Park (ends-with('Park') = true)
            String expr = "$.teams.*[?(@.id == 'BOS')].stadium.ends-with('Park')";
            assertTrue((boolean) jsonPath.select(baseball, expr).get(0));

            expr = "$.teams.*[?(@.id == 'COL')].stadium.ends-with('Park')";
            assertFalse((boolean) jsonPath.select(baseball, expr).get(0));

            expr = "$.teams.*[?(@.id == 'COL')].stadium.ends-with('Field')";
            assertTrue((boolean) jsonPath.select(baseball, expr).get(0));

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("is-array(): baseball")
    void testIsArrayFunction() {
        try {
            JsonObject baseball = getBaseballData();
            JsonPath jsonPath = getJsonPath();

            String expression = "$.divisions[0].teams.is-array()";
            boolean result = jsonPath.selectValue(baseball, expression);
            assertTrue(result);

            expression = "$.teams[0].is-array()";
            result = jsonPath.selectValue(baseball, expression);
            assertFalse(result);

            expression = "$.teams[0].id.is-array()";
            result = jsonPath.selectValue(baseball, expression);
            assertFalse(result);

        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("is-boolean(): books")
    void testIsBooleanFunction() {
        try {
            JsonObject books = getBooks();
            JsonPath jsonPath = getJsonPath();

            String expression = "$.store.book[0].in-stock.is-boolean()";
            boolean result = jsonPath.selectValue(books, expression);
            assertTrue(result);

            expression = "$.store.book[2].in-stock.is-boolean()";
            result = jsonPath.selectValue(books, expression);
            assertTrue(result);

            //yet the book's in-stock property value is false:
            expression = "$.store.book[2].in-stock";
            result = jsonPath.selectValue(books, expression);
            assertFalse(result);

            expression = "$.store.book.is-boolean()";
            result = jsonPath.selectValue(books, expression);
            assertFalse(result);

        } catch (Exception e) {
            fail(e);
        }
    }

    private JsonObject getBooks() throws Exception {
        return loadData("/books.json").asJsonObject();
    }

    private JsonArray getCompoundTest() throws Exception {
        return loadData("/compound-test.json").asJsonArray();
    }

    private JsonObject getDistinctValues() throws Exception {
        return loadData("/distinct-values.json").asJsonObject();
    }

    private JsonObject getBaseballData() throws Exception {
        return loadData("/baseball.json").asJsonObject();
    }
}
