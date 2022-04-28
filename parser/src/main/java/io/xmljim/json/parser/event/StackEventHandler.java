package io.xmljim.json.parser.event;

import io.xmljim.json.factory.parser.JsonEventParserException;
import io.xmljim.json.factory.parser.ParserSettings;
import io.xmljim.json.factory.parser.Statistics;
import io.xmljim.json.factory.parser.event.EventType;
import io.xmljim.json.factory.parser.event.JsonEvent;
import io.xmljim.json.model.NodeType;
import io.xmljim.json.parser.util.Timer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;


class StackEventHandler extends BaseEventHandler {

    private NodeType documentType = null;
    private final Deque<NodeType> objectStack = new ArrayDeque<>();
    private final Deque<String> keyStack = new ArrayDeque<>();
    private boolean keyBit = false;
    private boolean awaitingKey = false;
    private Object lastValue = null;
    private String lastKey = null;
    private final Timer<EventType> eventTimer = new Timer<>();
    private long eventCount = 0;

    public StackEventHandler() {
        super();
    }

    public StackEventHandler(ParserSettings settings) {
        super(settings);
    }

    @Override
    public void onNext(JsonEvent event) {
        eventCount++;
        switch (eventTimer.start(event.getEventType())) {
            case DOCUMENT_START -> handleDocumentStart(getDataValue(event));
            case DOCUMENT_END -> getAssembler().documentEnd();
            case OBJECT_START -> handleObjectStart();
            case OBJECT_END -> handleObjectEnd();
            case ARRAY_START -> handleArrayStart();
            case ARRAY_END -> handleArrayEnd(event);
            case STRING_START -> handleStringStart();
            case STRING_END -> handleStringEnd(event);
            case BOOLEAN_START -> handleBooleanStart();
            case BOOLEAN_END -> handleBooleanEnd(event);
            case NULL_START -> handleNullStart();
            case NULL_END -> handleNullEnd(event);
            case NUMBER_START -> handleNumberStart();
            case NUMBER_END -> handleNumberEnd(event);
            case ENTITY_END -> handleEntityEnd();
            case KEY_END -> handleKeyEnd();
            default -> throw new JsonEventParserException("Unknown Event Type: " + event.getEventType());
        }
        eventTimer.stop(event.getEventType());
        //done: get next;
        requestNext();
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setEventProcessingTime(eventTimer.get(TimeUnit.SECONDS));
        statistics.setEventCount(eventCount);
        statistics.setEventsSentPerSecond(eventCount / eventTimer.get(TimeUnit.SECONDS));

        eventTimer.getSequences().forEach(tick -> {
            statistics.setStatistic(Statistics.COMPONENT_EVENT_HANDLER, "EVENT_" + tick.getSubject().name() + " (" + tick.count() + ")", tick.get(TimeUnit.SECONDS));
        });

        return statistics;
    }

    private NodeType getDocumentType() {
        return documentType;
    }

    private NodeType[] getObjectStack() {
        return objectStack.toArray(new NodeType[]{});
    }

    private NodeType getTopOfObjectStack() {
        return objectStack.getLast();
    }

    private String[] getKeyStack() {
        return keyStack.toArray(new String[]{});
    }

    private String getKeyValue() {
        final NodeType objectType = objectStack.peek();
        return objectType != null ? objectType == NodeType.ARRAY ? keyStack.peek() : keyStack.pop() : null;
    }

    private String handleValue(JsonEvent event, NodeType type) {
        final NodeType popped = objectStack.pop();
        if (popped != type) {
            throwError(event, "JSON Stack Error: Expected value type " + type + ", but saw " + popped);
        }

        awaitingKey = true;
        setKeyBit(true);
        return getKeyValue();
    }

    private void handleObjectStart() {
        objectStack.push(NodeType.OBJECT);
        setKeyBit(true);
        awaitingKey = true;
        getAssembler().jsonObjectStart(keyStack.peek());
    }

    private void handleObjectEnd() {
        objectStack.pop();
        final String objKey = objectStack.peek() == NodeType.ARRAY ? keyStack.peek() : keyStack.size() > 0 ? keyStack.pop() : "$";
        getAssembler().jsonObjectEnd(objKey);
    }

    private void handleStringStart() {
        if (!isKey()) {
            objectStack.push(NodeType.STRING);
        }
    }

    private void handleStringEnd(JsonEvent event) {
        handleString(event, getDataValue(event));
    }

    private void handleArrayStart() {
        objectStack.push(NodeType.ARRAY);
        getAssembler().jsonArrayStart(keyStack.peek());
    }

    private void handleArrayEnd(JsonEvent event) {
        getAssembler().jsonArrayEnd(handleValue(event, NodeType.ARRAY));
    }

    private void handleBooleanStart() {
        objectStack.push(NodeType.BOOLEAN);
    }

    private void handleBooleanEnd(JsonEvent event) {
        getAssembler().valueBoolean(handleValue(event, NodeType.BOOLEAN), Boolean.parseBoolean(getDataValue(event)));
    }

    private void handleNullStart() {
        objectStack.push(NodeType.NULL);
    }

    private void handleNullEnd(JsonEvent event) {
        getAssembler().valueNull(handleValue(event, NodeType.NULL));
    }

    private void handleNumberStart() {
        objectStack.push(NodeType.NUMBER);
    }

    private void handleNumberEnd(JsonEvent event) {
        handleNumber(event, getDataValue(event));
    }

    private void handleEntityEnd() {
        setKeyBit(true);
        awaitingKey = true;
    }

    private void handleKeyEnd() {
        setKeyBit(false);
        awaitingKey = false;
    }

    private void handleString(JsonEvent event, String data) {
        if (isKey()) {
            keyStack.push(data);
            awaitingKey = false;
            setKeyBit(false);
            getAssembler().newKey(data);
        } else {
            final NodeType popped = objectStack.pop();
            if (popped != NodeType.STRING) {
                throwError(event, "JSON Stack Error: Expected value type " + NodeType.STRING + ", but saw " + popped);
            }

            try {
                lastKey = keyStack.peek();
                final String key = objectStack.peek() == NodeType.ARRAY ? keyStack.peek() : keyStack.pop();
                getAssembler().valueString(key, data);
                lastValue = data;
            } catch (Exception e) {
                throw new JsonEventParserException(event.getLineNumber(), event.getColumn(),
                    "ERROR at token: " + data + "; lastSuccess {key: " + lastKey + ", value: " + lastValue.toString() + "}");
            }

            awaitingKey = true;
            setKeyBit(true);
        }
    }

    private void handleNumber(JsonEvent event, String data) {
        final String key = handleValue(event, NodeType.NUMBER);

        if (data.contains(".")) {
            getAssembler().valueNumber(key, getSettings().floatingNumberStrategy().apply(data));
        } else {
            getAssembler().valueNumber(key, getSettings().fixedNumberStrategy().apply(data));
        }
    }

    private void handleDocumentStart(String data) {
        NodeType type = null;
        if (data.charAt(0) == '{') {
            type = NodeType.OBJECT;
            awaitingKey = true;
            setKeyBit(true);
        } else {
            type = NodeType.ARRAY;
        }

        documentType = type;
        objectStack.push(type);
        getAssembler().documentStart(type);
    }

    private boolean isKey() {
        return getKeyBit() && awaitingKey &&
            objectStack.peekFirst() != null && objectStack.peekFirst() == NodeType.OBJECT;
    }

    private void setKeyBit(final boolean bool) {
        this.keyBit = bool;
    }

    private boolean getKeyBit() {
        return keyBit;
    }

    private void throwError(JsonEvent event, String message) {
        throw new JsonEventParserException(event.getLineNumber(), event.getColumn(), message);
    }
}
