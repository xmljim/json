package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.service.JsonService;

public interface ParserFactory extends JsonService {
    ParserBuilder newParserBuilder();

    default Parser newParser() {
        return newParserBuilder().defaultParser();
    }

}
