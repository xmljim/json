package io.github.xmljim.json.factory.test;

import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(version = "0.1.1", service = ParserFactory.class)
public class TestServiceClass implements ParserFactory {

    public TestServiceClass() {
        //no-op
    }

    @Override
    public ParserBuilder newParserBuilder() {
        return null;
    }
}
