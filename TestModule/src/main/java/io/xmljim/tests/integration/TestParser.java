package io.xmljim.tests.integration;

import io.github.xmljim.json.factory.jsonpath.JsonPath;
import io.github.xmljim.json.factory.jsonpath.JsonPathFactory;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.service.ServiceManager;

public class TestParser {
    public static void main(String... args) {

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        System.out.println(factory != null);

        JsonPath path = ServiceManager.getProvider(JsonPathFactory.class).newJsonPath();
        System.out.println(path != null);
    }
}
