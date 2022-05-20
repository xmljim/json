package io.github.xmljim.json.parser;

import io.github.xmljim.json.factory.parser.NumericValueType;
import io.github.xmljim.json.factory.parser.Parser;
import io.github.xmljim.json.factory.parser.ParserBuilder;
import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;

import java.nio.charset.Charset;

class ParserBuilderImpl implements ParserBuilder {
    private ParserSettingsImpl parserSettings = ParserSettingsImpl.defaultSettings();
    private Parser parser;

    public ParserBuilderImpl() {

    }

    @Override
    public ParserBuilder withParser(Parser parser) {
        this.parser = parser;
        return this;
    }

    @Override
    public ParserBuilder setCharacterSet(Charset characterSet) {
        parserSettings.setCharacterSet(characterSet);
        return this;
    }

    @Override
    public ParserBuilder setEventHandler(EventHandler handler) {
        parserSettings.setEventHandler(handler);
        handler.setSettings(parserSettings);
        return this;
    }

    @Override
    public ParserBuilder setAssembler(Assembler<?> assembler) {
        parserSettings.setAssembler(assembler);
        assembler.setSettings(parserSettings);
        return this;
    }

    @Override
    public ParserBuilder withProcessor(Processor processor) {
        parserSettings.setProcessor(processor);
        processor.setSettings(parserSettings);
        return this;
    }

    @Override
    public ParserBuilder setFixedNumberStrategy(NumericValueType numberStrategy) {
        parserSettings.setFixedNumberStrategy(numberStrategy);
        return this;
    }

    @Override
    public ParserBuilder setFloatingNumberStrategy(NumericValueType numberStrategy) {
        parserSettings.setFloatingNumberStrategy(numberStrategy);
        return this;
    }

    @Override
    public ParserBuilder setUseStrict(boolean useStrict) {
        parserSettings.setUseStrict(useStrict);
        return this;
    }

    @Override
    public ParserBuilder setBlockCount(int blockCount) {
        parserSettings.setBlockCount(blockCount);
        return this;
    }

    @Override
    public ParserBuilder setMaxEventBufferCapacity(int capacity) {
        parserSettings.setMaxBufferEventCapacity(capacity);
        return this;
    }

    @Override
    public <T> ParserBuilder setSetting(String name, T value) {
        parserSettings.setSetting(name, value);
        return this;
    }

    @Override
    public ParserBuilder withDefaultSettings() {
        parserSettings = ParserSettingsImpl.defaultSettings();
        return this;
    }

    @Override
    public Parser build() {
        if (parser == null) {
            return new DefaultParser(parserSettings);
        } else {
            parser.setSettings(parserSettings);
            return parser;
        }
    }
}
