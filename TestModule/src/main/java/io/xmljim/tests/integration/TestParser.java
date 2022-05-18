package io.xmljim.tests.integration;

import io.xmljim.json.factory.parser.ParserFactory;
import io.xmljim.json.service.ServiceManager;

public class TestParser {
    public static void main(String... args) {

        ParserFactory factory = ServiceManager.getProvider(ParserFactory.class);
        System.out.println(factory != null);
    }
}
