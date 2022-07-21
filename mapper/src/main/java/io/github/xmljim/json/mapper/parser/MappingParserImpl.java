package io.github.xmljim.json.mapper.parser;

import io.github.xmljim.json.factory.mapper.Mapper;
import io.github.xmljim.json.factory.mapper.parser.MappingParser;
import io.github.xmljim.json.factory.parser.InputData;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.model.JsonObject;

public class MappingParserImpl implements MappingParser {
    private final Mapper mapper;
    private final Parser parser;

    public MappingParserImpl(Mapper mapper, Parser parser) {
        this.mapper = mapper;
        this.parser = parser;
    }

    @Override
    public <T> T parse(InputData inputData, Class<T> targetClass) {
        JsonObject jsonObject = parser.parse(inputData);
        return mapper.toClass(jsonObject, targetClass);
    }
}
