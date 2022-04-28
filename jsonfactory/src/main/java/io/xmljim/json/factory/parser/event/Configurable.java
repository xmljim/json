package io.xmljim.json.factory.parser.event;

import io.xmljim.json.factory.parser.ParserSettings;

public interface Configurable {

    void setSettings(ParserSettings settings);

    ParserSettings getSettings();
}
