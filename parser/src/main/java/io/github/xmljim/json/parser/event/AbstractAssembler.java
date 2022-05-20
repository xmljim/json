package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.event.Assembler;

abstract class AbstractAssembler<T> implements Assembler<T> {
    private ParserSettings settings;

    public void setSettings(ParserSettings settings) {
        this.settings = settings;
    }

    public ParserSettings getSettings() {
        return settings;
    }
}
