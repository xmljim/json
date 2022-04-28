package io.xmljim.json.factory.parser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public interface Parser {

    ParserSettings getSettings();

    void setSettings(ParserSettings settings);

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
            e.printStackTrace();
        }

        return result;

//        return (T) getSettings().getAssembler().getResult();
    }


    default void initializeProcessor() {
        getSettings().getProcessor().subscribe(getSettings().getEventHandler());
    }

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
