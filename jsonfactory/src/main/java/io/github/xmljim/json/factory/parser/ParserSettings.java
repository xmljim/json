package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;

import java.nio.charset.Charset;

public interface ParserSettings {
    boolean enableStatistics();

    NumericValueType fixedNumberStrategy();

    NumericValueType floatingNumberStrategy();

    Processor getProcessor();

    EventHandler getEventHandler();

    <T> Assembler<T> getAssembler();

    Charset getCharacterSet();

    int getMaxEventBufferCapacity();

    long getRequestNextLength();

    int getBlockCount();

    default int getBlockSizeBytes() {
        return getBlockCount() * 1024;
    }

    boolean useStrict();

    <T> T getSetting(String name);

}
