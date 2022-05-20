package io.xmljim.json.mapper.test;

import io.github.xmljim.json.factory.mapper.Mapper;
import io.github.xmljim.json.factory.mapper.MapperFactory;
import io.github.xmljim.json.factory.model.ElementFactory;
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.model.JsonObject;
import io.github.xmljim.json.service.ServiceManager;
import io.xmljim.json.mapper.test.testclasses.BasicTestClass;
import io.xmljim.json.mapper.test.testclasses.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mapper Tests")
class MapperTest {
    private Mapper mapper;

    @BeforeEach
    void onBeforeTest() {
        MapperFactory factory = ServiceManager.getProvider(MapperFactory.class);
        mapper = factory.newMapper();
    }

    @Test
    @DisplayName("Can Create a JSONArray from a collection of values")
    void testToJsonArrayFromCollection() {
        List<Object> testList = new ArrayList<>();
        testList.add("A string");
        testList.add(true);
        testList.add(42);
        testList.add(null);

        Map<String, Object> nested = new HashMap<>();
        nested.put("Boston", "Red Sox");
        nested.put("New York", "Yankees");
        nested.put("Denver", "Avalanche");
        testList.add(nested);

        List<Object> list2 = new ArrayList<>();
        list2.add("foo");
        list2.add(nested);

        testList.add(list2);

        JsonArray jsonArray = mapper.toJson(testList);

        assertTrue(jsonArray.get(4) instanceof JsonObject);
        assertEquals(true, jsonArray.get(1));
        assertEquals(testList.size(), jsonArray.size());

    }

    @Test
    @DisplayName("Can Create a JSONObject from a map of values")
    void testToJsonObjectFromMap() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("firstName", "Jim");
        map1.put("lastName", "Earley");
        map1.put("age", 42);
        map1.put("lovesToSki", true);
        map1.put("family", Arrays.stream(new String[]{"Jen", "Lauren", "Meaghan", "Nathan"}).collect(Collectors.toList()));
        map1.put("null", null);
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("Boston", "Red Sox");
        nested.put("Denver", "Avalanche");
        map1.put("teams", nested);

        JsonObject object1 = mapper.toJson(map1);
        assertEquals(object1.get("firstName"), map1.get("firstName"));

        System.out.println(object1.prettyPrint());
    }

    @Test
    @DisplayName("Can create a JSONObject from a class instance")
    void testToJsonObjectFromObject() {
        Person person = new Person();
        person.setFirstName("Johnny");
        person.setLastName("Marr");

        JsonObject jsonObject = mapper.toJson(person);
        assertNotNull(jsonObject);

        assertEquals(person.getFirstName(), jsonObject.get("firstName"));
        assertEquals(person.getLastName(), jsonObject.get("lastName"));

        System.out.println(jsonObject.prettyPrint());
    }

    @Test
    void toMap() {

    }

    @Test
    void toList() {
    }

    @Test
    void toValue() {
    }

    @Test
    void toClass() {
        ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
        final JsonObject basic = elementFactory.newObject();
        basic.put("message", "Hello World");
        basic.put("read", true);
        basic.put("length", getValue(60));
        basic.put("valueSet", Arrays.asList("foo", "bar", "baz", "buzz"));

        final JsonObject subClass = elementFactory.newObject();
        subClass.put("name", "Test 1");
        subClass.put("status", true);

        basic.put("subclass", subClass);

        MapperFactory factory = ServiceManager.getProvider(MapperFactory.class);
        Mapper mapper = factory.newBuilder().setTargetClass(BasicTestClass.class).build();
        BasicTestClass testClass = mapper.toClass(basic);

        assertNotNull(testClass);
        assertEquals(basic.get("message"), testClass.getMessage());
    }

    private long getValue(int val) {
        long value = 2;
        for (int i = 1; i <= val; i++) {
            value *= i;
        }

        return value;
    }
}