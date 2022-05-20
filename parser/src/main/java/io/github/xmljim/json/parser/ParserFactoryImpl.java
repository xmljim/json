package io.github.xmljim.json.parser;

import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.ParserFactory;
import io.github.xmljim.json.service.JsonServiceProvider;

@JsonServiceProvider(service = ParserFactory.class, version = "1.0.1")
public class ParserFactoryImpl implements ParserFactory {
    @Override
    public ParserBuilder newParserBuilder() {
        return new ParserBuilderImpl();
    }
}
