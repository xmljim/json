package io.github.xmljim.json.factory.parser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * A JSON Parser
 */
public interface Parser {

    /**
     * Return the Parser's settings
     *
     * @return the parser settings
     */
    ParserSettings getSettings();

    /**
     * Utility method to apply parser settings
     *
     * @param settings the parser settings
     */
    void setSettings(ParserSettings settings);

    /**
     * Parse the data
     *
     * @param data the input data
     * @param <T>  the data format
     * @return the parsed JSON data in the format requested
     */
    default <T> T parse(InputData data) {

        initializeProcessor();
        T result = null;
        getSettings().getProcessor().process(data.inputStream());
        @SuppressWarnings("unchecked")
        Supplier<T> supplier = (Supplier<T>) getSettings().getAssembler().getResult();
        CompletableFuture<T> future = CompletableFuture.supplyAsync(supplier);


        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new JsonParserException(e);
        }
        return result;

//        return (T) getSettings().getAssembler().getResult();
    }


    /**
     * Initialize the Processer used by this parser
     */
    default void initializeProcessor() {
        getSettings().getProcessor().subscribe(getSettings().getEventHandler());
    }

    /**
     * Return the statistics from the parsing
     *
     * @return the statistics
     */
    default Statistics getStatistics() {
        Statistics statistics = new Statistics();
        if (getSettings().enableStatistics()) {
            statistics.merge(getSettings().getProcessor().getStatistics());
            statistics.merge(getSettings().getAssembler().getStatistics());
            statistics.merge(getSettings().getEventHandler().getStatistics());
        }

        return statistics;
    }


}
