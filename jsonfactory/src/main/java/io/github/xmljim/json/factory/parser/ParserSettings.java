package io.github.xmljim.json.factory.parser;

import io.github.xmljim.json.factory.parser.event.Assembler;
import io.github.xmljim.json.factory.parser.event.EventHandler;
import io.github.xmljim.json.factory.parser.event.Processor;

import java.nio.charset.Charset;

/**
 * Parser settings. Note that not all Parsers or constituent components
 * orchestrated by the Parser will recognize or use these settings
 */
public interface ParserSettings {
    /**
     * Returns whether nor not to enable statistics
     *
     * @return true to enable statistics; false otherwise
     */
    boolean enableStatistics();

    /**
     * Return the fixed number strategy
     *
     * @return the fixed number strategy for Json number values
     */
    NumericValueType fixedNumberStrategy();

    /**
     * Return the floating number strategy
     *
     * @return the floating number strategy
     */
    NumericValueType floatingNumberStrategy();

    /**
     * Return the Processor to use
     *
     * @return the Processor
     */
    Processor getProcessor();

    /**
     * Return the Event Handler to use
     *
     * @return the Event Handler
     */
    EventHandler getEventHandler();

    /**
     * Return the Assembler to use
     *
     * @param <T> The assembler type/format
     * @return the Assembler
     */
    <T> Assembler<T> getAssembler();

    /**
     * Return the character set to use for parsing
     *
     * @return the character set
     */
    Charset getCharacterSet();

    /**
     * Return the Maximum Event Buffer Capacity to use with Event subscriptions
     *
     * @return the Maximum Event Buffer Capacity to use with Event subscriptions
     */
    int getMaxEventBufferCapacity();

    /**
     * Return the number of events to send via subscription
     *
     * @return the number of events
     */
    long getRequestNextLength();

    /**
     * Return block count
     *
     * @return the block count
     */
    int getBlockCount();

    /**
     * Return the block size
     *
     * @return the block size
     */
    default int getBlockSizeBytes() {
        return getBlockCount() * 1024;
    }

    /**
     * Return whether to use strict ECMA rules for parsing
     *
     * @return true if using strict; false otherwise
     */
    boolean useStrict();

    /**
     * Return a custom setting
     *
     * @param name the setting name
     * @param <T>  the value type
     * @return the setting value
     */
    <T> T getSetting(String name);

    ParserSettings merge(ParserSettings settings);

}
