package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.service.JsonService;

/**
 * Factory/Service for the Json Parser
 */
public interface ParserFactory extends JsonService {

    /**
     * Create a new builder to configure and build a Parser
     *
     * @return a new ParserBuilder
     */
    ParserBuilder newParserBuilder();

    /**
     * Create a new Parser with default settings
     *
     * @return a new Parser
     */
    default Parser newParser() {
        return newParserBuilder().defaultParser();
    }

}
