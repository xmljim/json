package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.event.Processor;

public abstract class Processors implements Processor {

    public static Processor newDefaultProcessor(ParserSettings settings) {
        return new BufferedEventProcessor(settings);
    }
}
