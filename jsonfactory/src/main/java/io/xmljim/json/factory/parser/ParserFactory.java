package io.xmljim.json.factory.parser;

import io.xmljim.json.service.JsonService;

public interface ParserFactory extends JsonService {
    ParserBuilder newParserBuilder();

    default Parser newParser() {
        return newParserBuilder().defaultParser();
    }
    
}
