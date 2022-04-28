package io.xmljim.json.parser;

import io.xmljim.json.factory.parser.Parser;
import io.xmljim.json.factory.parser.ParserSettings;

class DefaultParser implements Parser {
    private ParserSettings settings;

    public DefaultParser() {
        
    }

    public DefaultParser(ParserSettings settings) {
        this.settings = settings;
    }

    @Override
    public ParserSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(ParserSettings settings) {
        this.settings = settings;
    }
}
