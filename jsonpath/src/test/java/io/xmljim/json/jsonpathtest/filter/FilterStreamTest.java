package io.xmljim.json.jsonpathtest.filter;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.compiler.Compiler;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.FilterStream;
import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Settings;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.JsonObject;
import io.xmljim.json.service.ServiceManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterStreamTest {

    @Test
    void testSimpleChildKeyWithDot() {
        JsonObject object = createJsonObject();
        object.put("foo", "bar");
        String jsonPath = "$.foo";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals("bar", resultList.get(0).get().asJsonValue().get());
    }

    @Test
    void testInvalidChildKeyWithDot() {
        JsonObject object = createJsonObject();
        object.put("foo", "bar");
        String jsonPath = "$.boo";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(0, resultList.size());
    }

    @Test
    void testInvalidIndexOnValue() {
        JsonObject object = createJsonObject();
        object.put("foo", "bar");

        String jsonPath = "$.foo[0]";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(0, resultList.size());
    }

    @Test
    void testSimpleChildKeyBrackets() {
        JsonObject object = createJsonObject();
        object.put("foo", "bar");

        String jsonPath = "$['foo']";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals("bar", resultList.get(0).get().asJsonValue().get());
        assertEquals(jsonPath, resultList.get(0).path());
    }

    @Test
    void testSimpleChildIndex() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);

        String jsonPath = "$[1]";
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0).get().asJsonValue().get());
        assertEquals(jsonPath, resultList.get(0).path());
    }

    @Test
    void testInvalidChildIndex() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);

        String jsonPath = "$[99]";
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(0, resultList.size());
    }

    @Test
    void testNegativeChildIndex() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);

        String jsonPath = "$[-1]"; //last child
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals(3, resultList.get(0).get().asJsonValue().get());
        assertEquals(jsonPath, resultList.get(0).path());

        jsonPath = "$[-2]"; //second-to-last child
        filterStream = createFilterStream(jsonPath);
        results = filterStream.filter(array);
        resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0).get().asJsonValue().get());
        assertEquals(jsonPath, resultList.get(0).path());
    }

    @Test
    void testInvalidNegativeIndex() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);

        String jsonPath = "$[-4]"; //last child
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(0, resultList.size());
    }

    @Test
    void testNestedChildrenBrackets() {
        JsonObject object = createJsonObject();
        JsonObject barObject = createJsonObject();
        barObject.put("bar", "buzz");
        object.put("foo", barObject);

        String jsonPath = "$['foo']['bar']";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals("buzz", resultList.get(0).get().asJsonValue().get());
        assertEquals(jsonPath, resultList.get(0).path());
    }

    @Test
    void testNestedChildrenDots() {
        JsonObject object = createJsonObject();
        JsonObject barObject = createJsonObject();
        barObject.put("bar", "buzz");
        object.put("foo", barObject);

        String jsonPath = "$.foo.bar";
        FilterStream filterStream = createFilterStream(jsonPath);

        Stream<Context> results = filterStream.filter(object);
        List<Context> resultList = results.toList();
        assertEquals(1, resultList.size());
        assertEquals("buzz", resultList.get(0).get().asJsonValue().get());
        assertEquals("$['foo']['bar']", resultList.get(0).path());
    }

    @Test
    void testRecursionAtRoot() {
        JsonObject object = createJsonObject();
        JsonObject barObject = createJsonObject();
        barObject.put("bar", "buzz");
        object.put("foo", barObject);

        String jsonPath = "$..";
        FilterStream filterStream = createFilterStream(jsonPath);
        List<Context> results = filterStream.filter(object).toList();

        assertEquals(2, results.size());
        assertEquals(DataType.OBJECT, results.get(0).type());
        results.forEach(context -> System.out.println(context.path()));
    }

    @Test
    void testWildcardAtRoot() {
        JsonObject object = createJsonObject();
        JsonObject barObject = createJsonObject();
        barObject.put("bar", "buzz");
        object.put("foo", barObject);

        String jsonPath = "$.*";
        FilterStream filterStream = createFilterStream(jsonPath);
        List<Context> results = filterStream.filter(object).toList();

        assertEquals(1, results.size());
        assertEquals(DataType.OBJECT, results.get(0).type());
        results.forEach(context -> System.out.println(context.path() + ": " + context));
    }

    @Test
    void testWildcardAtRootMultipleKeys() {
        JsonObject object = createJsonObject();
        JsonObject barObject = createJsonObject();
        barObject.put("bar", "buzz");
        object.put("foo", barObject);
        object.put("bing", "bong");

        String jsonPath = "$.*";
        FilterStream filterStream = createFilterStream(jsonPath);
        List<Context> results = filterStream.filter(object).toList();

        assertEquals(2, results.size());
        assertEquals(DataType.OBJECT, results.get(0).type());
        results.forEach(context -> System.out.println(context.path() + ": " + context));
    }

    @Test
    void testWithUnionFilter() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);

        String jsonPath = "$[1,3]";
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(2, resultList.size());
        assertEquals(2, resultList.get(0).get().asJsonValue().get());
        assertEquals(4, resultList.get(1).get().asJsonValue().get());
        assertEquals("$[1]", resultList.get(0).path());
        assertEquals("$[3]", resultList.get(1).path());
    }

    @Test
    void testWithSliceLastTwoItems() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);

        String jsonPath = "$[-2:]";
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(2, resultList.size());
        assertEquals(3, resultList.get(0).get().asJsonValue().get());
        assertEquals(4, resultList.get(1).get().asJsonValue().get());
        assertEquals("$[2]", resultList.get(0).path());
        assertEquals("$[3]", resultList.get(1).path());
    }

    @Test
    void testSliceWithFirstTwoItems() {
        JsonArray array = createJsonArray();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);

        String jsonPath = "$[:2]";
        FilterStream filterStream = createFilterStream(jsonPath);
        Stream<Context> results = filterStream.filter(array);
        List<Context> resultList = results.toList();
        assertEquals(2, resultList.size());
        assertEquals(1, resultList.get(0).get().asJsonValue().get());
        assertEquals(2, resultList.get(1).get().asJsonValue().get());
        assertEquals("$[0]", resultList.get(0).path());
        assertEquals("$[1]", resultList.get(1).path());
    }

    private JsonObject createJsonObject() {
        ElementFactory factory = ServiceManager.getProvider(ElementFactory.class);
        return factory.newObject();
    }

    private JsonArray createJsonArray() {
        ElementFactory factory = ServiceManager.getProvider(ElementFactory.class);
        return factory.newArray();
    }

    private FilterStream createFilterStream(String expression) {
        return Compiler.newPathCompiler(expression, new Settings()).compile();
    }
}