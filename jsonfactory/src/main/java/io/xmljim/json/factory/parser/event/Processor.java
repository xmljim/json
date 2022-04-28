package io.xmljim.json.factory.parser.event;

import io.xmljim.json.factory.parser.Statistics;

import java.io.InputStream;

public interface Processor extends EventPublisher, Configurable {

    void process(InputStream inputStream);

    Statistics getStatistics();
}
