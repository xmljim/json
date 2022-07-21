package io.github.xmljim.json.factory.parser.event;

import java.util.concurrent.Flow;

/**
 * Tagging interface that extends the Flow.Publisher interface
 */
public interface EventPublisher extends Flow.Publisher<JsonEvent> {

}
