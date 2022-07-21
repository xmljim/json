package io.github.xmljim.json.factory.parser.event;

import io.github.xmljim.json.factory.parser.Statistics;

import java.util.concurrent.Flow;

/**
 * Interface for Event Handlers. It extends the Flow API Subscriber interface so that
 * it can receive events from a {@link Processor} (which is a {@link Flow.Publisher})
 */
public interface EventHandler extends Flow.Subscriber<JsonEvent>, Configurable {

    /**
     * Returns whether the event handler is complete
     *
     * @return true if complete
     */
    boolean isComplete();

    /**
     * Return the Assembler associated with this event handler
     *
     * @return the Assembler
     */
    Assembler<?> getAssembler();

    /**
     * Return the statistics from this event handler
     *
     * @return the statistics
     */
    Statistics getStatistics();
}
