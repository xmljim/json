package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;

import java.nio.charset.Charset;

/**
 * Builder interface for creating a Parser with given settings
 */
public interface ParserBuilder {

    /**
     * Specify a parser implementation
     *
     * @param parser the parser
     * @return the builder
     */
    ParserBuilder withParser(Parser parser);

    /**
     * Specify the JSON character set to use
     *
     * @param characterSet the character set
     * @return the builder
     */
    ParserBuilder setCharacterSet(Charset characterSet);

    /**
     * Specify the Event Handler implementation to use
     *
     * @param handler the event handler
     * @return the builder
     */
    ParserBuilder setEventHandler(EventHandler handler);

    /**
     * Specify the Assembler to use with the Event Handler
     *
     * @param assembler the assembler
     * @return the builder
     */
    ParserBuilder setAssembler(Assembler<?> assembler);

    /**
     * Specify a Processor that will process the JSON data
     *
     * @param processor the processor
     * @return the builder
     */
    ParserBuilder withProcessor(Processor processor);

    /**
     * Specify a fixed number strategy (i.e., use a Long or Integer as a default)
     *
     * @param numberStrategy the number strategy
     * @return the builder
     */
    ParserBuilder setFixedNumberStrategy(NumericValueType numberStrategy);

    /**
     * Specify a floating number strategy (i.e., Float or Double)
     *
     * @param numberStrategy the number strategy
     * @return the builder
     */
    ParserBuilder setFloatingNumberStrategy(NumericValueType numberStrategy);

    /**
     * Specify whether or not use strict EMCA encoding rules
     *
     * @param useStrict true to use strict, false otherwise
     * @return the builder
     */
    ParserBuilder setUseStrict(boolean useStrict);

    /**
     * Specify the block size to use with the Processor to process data.
     * Note: Not all Processors will recognize this value
     *
     * @param blockCount the block size
     * @return the builder
     */
    ParserBuilder setBlockCount(int blockCount);

    /**
     * Specify the MaxEventBufferCapacity to be used by EventHandlers for subscribed events.
     * Not: Not all EventHandlers will recognize or use this value
     *
     * @param capacity the capacity
     * @return the builder
     */
    ParserBuilder setMaxEventBufferCapacity(int capacity);

    /**
     * Specify a custom setting. These will be implementation specific
     *
     * @param name  the setting name
     * @param value the setting value
     * @param <T>   The value type
     * @return the bulder
     */
    <T> ParserBuilder setSetting(String name, T value);

    /**
     * Return the default settings
     *
     * @return the builder
     */
    ParserBuilder withDefaultSettings();

    default ParserBuilder mergeSettings(ParserSettings settings) {
        if (settings.getAssembler() != null) {
            setAssembler(settings.getAssembler());
        }

        setBlockCount(settings.getBlockCount());

        if (settings.getCharacterSet() != null) {
            setCharacterSet(settings.getCharacterSet());
        }

        if (settings.getEventHandler() != null) {
            setEventHandler(settings.getEventHandler());
        }

        if (settings.fixedNumberStrategy() != null) {
            setFixedNumberStrategy(settings.fixedNumberStrategy());
        }

        if (settings.floatingNumberStrategy() != null) {
            setFloatingNumberStrategy(settings.floatingNumberStrategy());
        }

        setMaxEventBufferCapacity(settings.getMaxEventBufferCapacity());
        setUseStrict(settings.useStrict());

        if (settings.getProcessor() != null) {
            withProcessor(settings.getProcessor());
        }
        return this;
    }

    /**
     * Create a new Parser
     *
     * @return
     */
    Parser build();

    /**
     * Create a parser with default settings.
     *
     * @return
     */
    default Parser defaultParser() {
        return withDefaultSettings().build();
    }
}
