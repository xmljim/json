package io.github.xmljim.json.factory.parser.event;

import io.github.xmljim.json.factory.parser.ParserSettings;

/**
 * Wrapper interface for configuration
 */
public interface Configurable {

    /**
     * Set settings
     *
     * @param settings the parser settings
     */
    void setSettings(ParserSettings settings);

    /**
     * return the Parser settings
     *
     * @return the parser settings
     */
    ParserSettings getSettings();
}
