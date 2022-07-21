package io.github.xmljim.json.api.test;

import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.model.JsonValue;
import io.github.xmljim.json.model.NodeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static io.github.xmljim.json.api.JsonApi.JsonElement;
import static io.github.xmljim.json.api.JsonApi.JsonParser;
import static org.junit.jupiter.api.Assertions.*;

public class JsonApiTests {

    @Test
    @DisplayName("JsonElement - New Array")
    void testCreateNewArray() {
        JsonArray array = JsonElement.newArray();
        assertNotNull(array);
        assertEquals(NodeType.ARRAY, array.type());
        assertTrue(array.type().isArray());
    }

    @Test
    @DisplayName("JsonElement - New Object")
    void testCreateNewObject() {
        JsonObject object = JsonElement.newObject();
        assertNotNull(object);
        assertEquals(NodeType.OBJECT, object.type());
        assertTrue(object.type().isObject());
    }

    @Test
    @DisplayName("JsonElement - String Value")
    void testCreateStringValue() {
        String value = "TestString";
        JsonValue<String> stringJsonValue = JsonElement.newValue(value);
        assertNotNull(stringJsonValue);
        assertEquals(NodeType.STRING, stringJsonValue.type());
        assertEquals(value, stringJsonValue.get());
        assertTrue(stringJsonValue.type().isPrimitive());
    }

    @Test
    @DisplayName("JsonElement - Boolean Value")
    void testCreateBooleanValue() {
        boolean value = true;
        JsonValue<Boolean> booleanJsonValue = JsonElement.newValue(value);
        assertNotNull(booleanJsonValue);
        assertEquals(NodeType.BOOLEAN, booleanJsonValue.type());
        assertEquals(value, booleanJsonValue.get());
        assertTrue(booleanJsonValue.type().isPrimitive());
    }

    @Test
    @DisplayName("JsonElement - Long Value")
    void testCreateLongValue() {
        Long value = 1L;
        JsonValue<Long> jsonValue = JsonElement.newValue(value);
        assertNotNull(jsonValue);
        assertEquals(NodeType.LONG, jsonValue.type());
        assertEquals(value, jsonValue.get());
        assertTrue(jsonValue.type().isPrimitive());
        assertTrue(jsonValue.type().isNumeric());
    }

    @Test
    @DisplayName("JsonElement - Integer Value")
    void testCreateIntegerValue() {
        int value = 1;
        JsonValue<Integer> jsonValue = JsonElement.newValue(value);
        assertNotNull(jsonValue);
        assertEquals(NodeType.INTEGER, jsonValue.type());
        assertEquals(value, jsonValue.get());
        assertTrue(jsonValue.type().isPrimitive());
        assertTrue(jsonValue.type().isNumeric());
    }

    @Test
    @DisplayName("JsonElement - Double Value")
    void testCreateDoubleValue() {
        double value = 3.1415926;
        JsonValue<Double> jsonValue = JsonElement.newValue(value);
        assertNotNull(jsonValue);
        assertEquals(NodeType.DOUBLE, jsonValue.type());
        assertEquals(value, jsonValue.get());
        assertTrue(jsonValue.type().isPrimitive());
        assertTrue(jsonValue.type().isNumeric());
    }

    @Test
    @DisplayName("JsonElement - Null Value")
    void testCreateNullValue() {
        Object value = null;
        JsonValue<?> jsonValue = JsonElement.newValue(value);
        assertNotNull(jsonValue);
        assertEquals(NodeType.NULL, jsonValue.type());
        assertNull(jsonValue.get());
        assertTrue(jsonValue.type().isPrimitive());
    }

    @Test
    @DisplayName("JsonElement - JsonArray Value")
    void testCreateArrayValue() {
        JsonArray array = JsonElement.newArray();
        JsonValue<JsonArray> jsonValue = JsonElement.newValue(array);
        assertNotNull(jsonValue);
        assertEquals(NodeType.ARRAY, jsonValue.type());
        assertEquals(array, jsonValue.get());
        assertTrue(jsonValue.type().isArray());
    }

    @Test
    @DisplayName("JsonElement - JsonObject Value")
    void testCreateObjectValue() {
        JsonObject value = JsonElement.newObject();
        JsonValue<JsonObject> jsonValue = JsonElement.newValue(value);
        assertNotNull(jsonValue);
        assertEquals(NodeType.OBJECT, jsonValue.type());
        assertEquals(value, jsonValue.get());
        assertTrue(jsonValue.type().isObject());
    }

    @Test
    @DisplayName("JsonParser - Parse String")
    void testParserString() {
        String jsonString = """
            {"a": 1, "b": true, "c": null, "d": {"foo": "bar"}, "e": [1, true, null, {"foo": "bar"}, [3.14, 1.62]]}
            """;

        JsonObject jsonObject = JsonParser.parse(jsonString);

        assertNotNull(jsonObject);
        assertEquals(1, (long) jsonObject.get("a"));
        assertTrue((boolean) jsonObject.get("b"));
    }

    @Test
    @DisplayName("JsonParser - Parse InputStream")
    void testParserInputStream() {
        try (InputStream inputStream = getClass().getResourceAsStream("/test1.json")) {
            JsonObject jsonObject = JsonParser.parse(inputStream);
            assertNotNull(jsonObject);
            assertEquals(1, (long) jsonObject.get("a"));
            assertTrue((boolean) jsonObject.get("b"));
        } catch (IOException ioException) {
            fail();
        }
    }
}
