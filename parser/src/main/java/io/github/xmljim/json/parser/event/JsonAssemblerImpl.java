package io.github.xmljim.json.parser.event;

import io.github.xmljim.json.factory.parser.Statistics;
import io.github.xmljim.json.model.*;
import io.github.xmljim.json.parser.util.Timer;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

class JsonAssemblerImpl extends AbstractJsonAssembler {
    private JsonNode currentNode;
    private final ArrayDeque<JsonNode> stack = new ArrayDeque<>();

    private long entities;
    private final Timer<JsonAssemblerImpl> assemblerTimer = new Timer<>();

    @Override
    public void documentStart(NodeType type) {
        //result = (JsonNode) eventTimer.start(type == NodeType.ARRAY ? getElementFactory().newArray() : getElementFactory().newObject());
        assemblerTimer.start(this);
        result = type == NodeType.ARRAY ? getElementFactory().newArray() : getElementFactory().newObject();
        entities++;
        stack.push(result);
        assemblerTimer.stop(this);
        currentNode = result;

    }

    @Override
    public void jsonArrayStart(String key) {
        assemblerTimer.start(this);

        JsonArray array = getElementFactory().newArray();
        appendToCurrent(key, array);
        assemblerTimer.stop(this);
    }

    @Override
    public void jsonArrayEnd(String key) {
        assemblerTimer.start(this);
        JsonArray array = (JsonArray) stack.pop();
        currentNode = stack.peek();
        assemblerTimer.stop(this);
    }

    @Override
    public void jsonObjectStart(String key) {
        assemblerTimer.start(this);
        JsonObject object = getElementFactory().newObject();
        appendToCurrent(key, object);
        assemblerTimer.stop(this);
    }

    @Override
    public void jsonObjectEnd(String key) {
        assemblerTimer.start(this);
        JsonObject object = (JsonObject) stack.pop();
        currentNode = stack.peek();
        assemblerTimer.stop(this);
    }

    @Override
    public void valueString(String key, String value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueLong(String key, Long value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueInt(String key, Integer value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueBigDecimal(String key, BigDecimal value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueDouble(String key, Double value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueFloat(String key, Float value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueNumber(String key, Number value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueBoolean(String key, boolean value) {
        assemblerTimer.start(this);
        createAndAppendValue(key, value);
        assemblerTimer.stop(this);
    }

    @Override
    public void valueNull(String key) {
        assemblerTimer.start(this);
        createAndAppendValue(key, null);
        assemblerTimer.stop(this);
    }

    @Override
    public void newKey(String key) {
        //no-op
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setAssemblyTime(assemblerTimer.get(TimeUnit.SECONDS));
        statistics.setEntityCount(entities);
        statistics.setAssemblyEntitiesPerSecond(entities / assemblerTimer.get(TimeUnit.SECONDS));

        return statistics;
    }

    private void createAndAppendValue(String key, Object value) {
        JsonValue<?> val = getElementFactory().newValue(value);
        appendToCurrent(key, val);
    }

    private void appendToCurrent(String key, JsonArray array) {
        if (currentNode instanceof JsonArray jsonArray) {
            jsonArray.add(array);
        } else if (currentNode instanceof JsonObject jsonObject) {
            jsonObject.put(key, array);
        }
        entities++;
        stack.push(array);
        currentNode = array;

    }

    private void appendToCurrent(String key, JsonObject obj) {
        if (currentNode instanceof JsonArray jsonArray) {
            jsonArray.add(obj);
        } else if (currentNode instanceof JsonObject jsonObject) {
            jsonObject.put(key, obj);
        }

        entities++;
        stack.push(obj);
        currentNode = obj;
    }

    private void appendToCurrent(String key, JsonValue<?> value) {

        if (currentNode instanceof JsonArray jsonArray) {
            jsonArray.add(value);
        } else if (currentNode instanceof JsonObject jsonObject) {
            jsonObject.put(key, value);
        }

        entities++;
    }
}
