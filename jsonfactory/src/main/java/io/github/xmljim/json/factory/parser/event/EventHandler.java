package io.github.xmljim.json.factory.parser.event;

import io.github.xmljim.json.factory.parser.Statistics;

import java.util.concurrent.Flow;

public interface EventHandler extends Flow.Subscriber<JsonEvent>, Configurable {

    boolean isComplete();

    Assembler<?> getAssembler();

    Statistics getStatistics();
}
