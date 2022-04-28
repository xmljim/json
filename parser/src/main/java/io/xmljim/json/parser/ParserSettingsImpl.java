package io.xmljim.json.parser;

import io.xmljim.json.factory.parser.FixedNumberValueType;
import io.xmljim.json.factory.parser.FloatingNumberValueType;
import io.xmljim.json.factory.parser.NumericValueType;
import io.xmljim.json.factory.parser.ParserSettings;
import io.xmljim.json.factory.parser.event.Assembler;
import io.xmljim.json.factory.parser.event.EventHandler;
import io.xmljim.json.factory.parser.event.Processor;
import io.xmljim.json.parser.event.Assemblers;
import io.xmljim.json.parser.event.EventHandlers;
import io.xmljim.json.parser.event.Processors;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class ParserSettingsImpl implements ParserSettings {
    private boolean enableStatistics;
    private NumericValueType fixedNumberStrategy;
    private NumericValueType floatingNumberStrategy;
    private Processor processor;
    private EventHandler eventHandler;
    private Assembler<?> assembler;
    private Charset charset;
    private int maxBufferEventCapacity;
    private long nextRequestLength;
    private int blockCount;
    private boolean useStrict;
    private final Map<String, Object> extraSettings = new HashMap<>();


    @Override
    public boolean enableStatistics() {
        return enableStatistics;
    }

    protected void setEnableStatistics(boolean enableStatistics) {
        this.enableStatistics = enableStatistics;
    }

    @Override
    public NumericValueType fixedNumberStrategy() {
        return fixedNumberStrategy;
    }

    protected void setFixedNumberStrategy(NumericValueType fixedNumberStrategy) {
        this.fixedNumberStrategy = fixedNumberStrategy;
    }

    @Override
    public NumericValueType floatingNumberStrategy() {
        return floatingNumberStrategy;
    }

    protected void setFloatingNumberStrategy(NumericValueType floatingNumberStrategy) {
        this.floatingNumberStrategy = floatingNumberStrategy;
    }

    @Override
    public Processor getProcessor() {
        return processor;
    }

    protected void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public EventHandler getEventHandler() {
        return eventHandler;
    }

    protected void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Assembler<T> getAssembler() {
        return (Assembler<T>) assembler;
    }

    protected <T> void setAssembler(Assembler<T> assembler) {
        this.assembler = assembler;
    }

    @Override
    public Charset getCharacterSet() {
        return charset;
    }

    protected void setCharacterSet(Charset charset) {
        this.charset = charset;
    }

    @Override
    public int getMaxEventBufferCapacity() {
        return maxBufferEventCapacity;
    }

    protected void setMaxBufferEventCapacity(int capacity) {
        this.maxBufferEventCapacity = capacity;
    }

    @Override
    public long getRequestNextLength() {
        return nextRequestLength;
    }

    protected void setNextRequestLength(long requestLength) {
        this.nextRequestLength = requestLength;
    }

    @Override
    public int getBlockCount() {
        return blockCount;
    }

    protected void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    @Override
    public boolean useStrict() {
        return useStrict;
    }

    protected void setUseStrict(boolean useStrict) {
        this.useStrict = useStrict;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSetting(String name) {
        return (T) extraSettings.getOrDefault(name, null);
    }

    protected <T> void setSetting(String name, T value) {
        extraSettings.put(name, value);
    }

    public static ParserSettingsImpl defaultSettings() {
        ParserSettingsImpl settings = new ParserSettingsImpl();
        settings.setUseStrict(true);
        settings.setFixedNumberStrategy(FixedNumberValueType.LONG);
        settings.setFloatingNumberStrategy(FloatingNumberValueType.DOUBLE);
        settings.setBlockCount(4);
        settings.setEnableStatistics(true);
        settings.setMaxBufferEventCapacity(32);
        settings.setNextRequestLength(1);
        settings.setCharacterSet(StandardCharsets.UTF_8);
        settings.setAssembler(Assemblers.newDefaultAssembler());
        settings.setEventHandler(EventHandlers.defaultEventHandler(settings));
        settings.setProcessor(Processors.newDefaultProcessor(settings));

        return settings;
    }
}
