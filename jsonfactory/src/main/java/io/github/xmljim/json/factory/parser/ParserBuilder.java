package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;

import java.nio.charset.Charset;

public interface ParserBuilder {

    ParserBuilder withParser(Parser parser);

    ParserBuilder setCharacterSet(Charset characterSet);

    ParserBuilder setEventHandler(EventHandler handler);

    ParserBuilder setAssembler(Assembler<?> assembler);

    ParserBuilder withProcessor(Processor processor);

    ParserBuilder setFixedNumberStrategy(NumericValueType numberStrategy);

    ParserBuilder setFloatingNumberStrategy(NumericValueType numberStrategy);

    ParserBuilder setUseStrict(boolean useStrict);

    ParserBuilder setBlockCount(int blockCount);

    ParserBuilder setMaxEventBufferCapacity(int capacity);

    <T> ParserBuilder setSetting(String name, T value);

    ParserBuilder withDefaultSettings();

    Parser build();

    default Parser defaultParser() {
        return withDefaultSettings().build();
    }
}
