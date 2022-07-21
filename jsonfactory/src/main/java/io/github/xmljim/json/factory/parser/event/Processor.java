package io.github.xmljim.json.factory.parser.event;

import io.github.xmljim.json.factory.parser.Statistics;

import java.io.InputStream;

/**
 * The Json Processer, which processes the JSON data and passes the data
 * as events to an {@link EventHandler}
 */
public interface Processor extends EventPublisher, Configurable {

    /**
     * Begin processing the data
     *
     * @param inputStream the input stream containing the JSON data
     */
    void process(InputStream inputStream);

    /**
     * Return the Statistics associated with this processor
     *
     * @return the Statistics associated with this processor
     */
    Statistics getStatistics();
}
