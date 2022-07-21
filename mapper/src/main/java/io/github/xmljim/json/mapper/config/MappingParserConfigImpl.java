package io.github.xmljim.json.mapper.config;

import io.github.xmljim.json.factory.config.AbstractConfiguration;
import io.github.xmljim.json.factory.mapper.ClassConfig;
import io.github.xmljim.json.factory.mapper.MappingParserConfig;
import io.github.xmljim.json.factory.parser.*;
import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;
import io.github.xmljim.json.service.JsonServiceProvider;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MappingParserConfigImpl extends AbstractConfiguration implements MappingParserConfig {

    private final List<ClassConfig> classConfigurations = new ArrayList<>();

    @Override
    public List<ClassConfig> getClassConfigurations() {
        return classConfigurations;
    }

    @Override
    public boolean enableStatistics() {
        return getOrDefault(MappingParserConfigFields.ENABLE_STATISTICS, MappingParserConfigFields.ENABLE_STATISTICS.defaultValue());
    }

    @Override
    public NumericValueType fixedNumberStrategy() {
        return getOrDefault(MappingParserConfigFields.FIXED_NUMBER_STRATEGY, MappingParserConfigFields.FIXED_NUMBER_STRATEGY.defaultValue());
    }

    @Override
    public NumericValueType floatingNumberStrategy() {
        return getOrDefault(MappingParserConfigFields.FLOATING_NUMBER_STRATEGY, MappingParserConfigFields.FLOATING_NUMBER_STRATEGY.defaultValue());
    }

    @Override
    public Processor getProcessor() {
        return getOrDefault(MappingParserConfigFields.PROCESSOR, MappingParserConfigFields.PROCESSOR.defaultValue());
    }

    @Override
    public EventHandler getEventHandler() {
        return getOrDefault(MappingParserConfigFields.EVENT_HANDLER, MappingParserConfigFields.EVENT_HANDLER.defaultValue());
    }

    @Override
    public <T> Assembler<T> getAssembler() {
        return getOrDefault(MappingParserConfigFields.ASSEMBLER, MappingParserConfigFields.ASSEMBLER.defaultValue());
    }

    @Override
    public Charset getCharacterSet() {
        return getOrDefault(MappingParserConfigFields.CHARACTER_SET, MappingParserConfigFields.CHARACTER_SET.defaultValue());
    }

    @Override
    public int getMaxEventBufferCapacity() {
        return getOrDefault(MappingParserConfigFields.MAX_EVENT_BUFFER_CAPACITY, MappingParserConfigFields.MAX_EVENT_BUFFER_CAPACITY.defaultValue());
    }

    @Override
    public long getRequestNextLength() {
        return getOrDefault(MappingParserConfigFields.REQUEST_NEXT_LENGTH, MappingParserConfigFields.REQUEST_NEXT_LENGTH.defaultValue());
    }

    @Override
    public int getBlockCount() {
        return getOrDefault(MappingParserConfigFields.BLOCK_COUNT, MappingParserConfigFields.BLOCK_COUNT.defaultValue());
    }

    @Override
    public boolean useStrict() {
        return getOrDefault(MappingParserConfigFields.USE_STRICT, MappingParserConfigFields.USE_STRICT.defaultValue());
    }

    @Override
    public <T> T getSetting(String name) {
        return get(name);
    }

    @Override
    public ParserSettings merge(ParserSettings settings) {
        return this;
    }

    @JsonServiceProvider(service = MappingParserConfig.Builder.class, version = "1.0.1", isNative = true)
    public static class MappingParserConfigBuilder implements MappingParserConfig.Builder {

        private final MappingParserConfigImpl mappingParserConfig = new MappingParserConfigImpl();

        @Override
        public Builder appendClassConfig(ClassConfig classConfig) {
            mappingParserConfig.classConfigurations.add(classConfig);
            return this;
        }

        @Override
        public Builder assembler(Assembler<?> assembler) {
            mappingParserConfig.put(MappingParserConfigFields.ASSEMBLER, assembler);
            return this;
        }

        @Override
        public Builder blockCount(int blockCount) {
            mappingParserConfig.put(MappingParserConfigFields.BLOCK_COUNT, blockCount);
            return this;
        }

        @Override
        public Builder characterSet(Charset charset) {
            mappingParserConfig.put(MappingParserConfigFields.CHARACTER_SET, charset);
            return this;
        }

        @Override
        public Builder enableStatistics(boolean enableStatistics) {
            mappingParserConfig.put(MappingParserConfigFields.ENABLE_STATISTICS, enableStatistics);
            return this;
        }

        @Override
        public Builder eventHandler(EventHandler eventHandler) {
            mappingParserConfig.put(MappingParserConfigFields.EVENT_HANDLER, eventHandler);
            return this;
        }

        @Override
        public Builder fixedNumberStrategy(NumericValueType fixedNumberStrategy) {
            mappingParserConfig.put(MappingParserConfigFields.FIXED_NUMBER_STRATEGY, fixedNumberStrategy);
            return this;
        }

        @Override
        public Builder floatingNumberStrategy(NumericValueType floatingNumberStrategy) {
            mappingParserConfig.put(MappingParserConfigFields.FLOATING_NUMBER_STRATEGY, floatingNumberStrategy);
            return this;
        }

        @Override
        public Builder maxEventBufferCapacity(int maxEventBufferCapacity) {
            mappingParserConfig.put(MappingParserConfigFields.MAX_EVENT_BUFFER_CAPACITY, maxEventBufferCapacity);
            return this;
        }

        @Override
        public Builder useStrict(boolean useStrict) {
            mappingParserConfig.put(MappingParserConfigFields.USE_STRICT, useStrict);
            return this;
        }

        @Override
        public Builder parser(Parser parser) {
            mappingParserConfig.put(MappingParserConfigFields.PARSER, parser);
            return this;
        }

        @Override
        public Builder processor(Processor processor) {
            mappingParserConfig.put(MappingParserConfigFields.PROCESSOR, processor);
            return this;
        }

        @Override
        public Builder requestNextLength(int requestNextLength) {
            mappingParserConfig.put(MappingParserConfigFields.REQUEST_NEXT_LENGTH, requestNextLength);
            return this;
        }

        @Override
        public MappingParserConfig build() {
            return mappingParserConfig;
        }
    }

    private enum MappingParserConfigFields {
        ASSEMBLER(null),
        BLOCK_COUNT(4),
        CHARACTER_SET(StandardCharsets.UTF_8),
        EVENT_HANDLER(null),

        ENABLE_STATISTICS(true),
        FIXED_NUMBER_STRATEGY(FixedNumberValueType.INTEGER),
        FLOATING_NUMBER_STRATEGY(FloatingNumberValueType.DOUBLE),
        MAX_EVENT_BUFFER_CAPACITY(32),
        PARSER(null),
        PROCESSOR(null),
        REQUEST_NEXT_LENGTH(1),
        USE_STRICT(true);
        private final Object defaultValue;

        @SuppressWarnings("unchecked")
        public <T> T defaultValue() {
            return (T) defaultValue;
        }

        <T> MappingParserConfigFields(T defaultValue) {
            this.defaultValue = defaultValue;
        }
    }
}
