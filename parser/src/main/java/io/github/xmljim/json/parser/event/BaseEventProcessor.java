package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.ParserSettings;
import io.github.xmljim.json.factory.parser.event.JsonEvent;
import io.github.xmljim.json.factory.parser.event.Processor;
import io.github.xmljim.json.parser.util.Timer;

import java.nio.ByteBuffer;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;

import static io.github.xmljim.json.factory.parser.event.EventType.*;

@SuppressWarnings("unused")
abstract class BaseEventProcessor extends SubmissionPublisher<JsonEvent> implements Processor {

    private final Timer<JsonEvent> eventTimer = new Timer<>();
    private ParserSettings settings;

    private JsonEvent lastEvent;

    public BaseEventProcessor(final ParserSettings settings) {
        super(ForkJoinPool.commonPool(), settings.getMaxEventBufferCapacity());
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

    public long getSendTime() {
        return eventTimer.getAccumulatedTime();
    }

    /**
     * Send the event to the recipient based on the settings EventStrategy
     *
     * @param event the event
     */
    protected void sendEvent(JsonEvent event) {
        submit(event);
        lastEvent = event;
    }

    protected JsonEvent getLastEvent() {
        return lastEvent;
    }

    protected void fireArrayEndEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, ARRAY_END));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireArrayStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, ARRAY_START));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireBooleanStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, BOOLEAN_START));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireBooleanEndEvent(ByteBuffer data, int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new DataEvent(lineNumber, column, BOOLEAN_END, data));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireDocumentEndEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, DOCUMENT_END));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireDocumentStartEvent(ByteBuffer data, int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new DataEvent(lineNumber, column, DOCUMENT_START, data));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireEntityEndEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, ENTITY_END));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireKeyEndEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, KEY_END));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireMapEndEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, OBJECT_END));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireMapStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, OBJECT_START));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireNullEndEvent(ByteBuffer data, int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new DataEvent(lineNumber, column, NULL_END, data));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireNullStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, NULL_START));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireNumberStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, NUMBER_START));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireNumberEndEvent(ByteBuffer data, int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new DataEvent(lineNumber, column, NUMBER_END, data));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireStringEndEvent(ByteBuffer data, int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new DataEvent(lineNumber, column, STRING_END, data));
        sendEvent(event);
        eventTimer.stop(event);
    }

    protected void fireStringStartEvent(int lineNumber, int column) {
        JsonEvent event = eventTimer.start(new TokenEvent(lineNumber, column, STRING_START));
        sendEvent(event);
        eventTimer.stop(event);
    }
}
