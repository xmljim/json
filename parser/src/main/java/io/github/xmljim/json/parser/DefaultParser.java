package io.github.xmljim.json.parser;

import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserSettings;

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
        if (settings != null) {
        }
        this.settings = settings;
    }
}
