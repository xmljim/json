package io.github.xmljim.json.factory.parser;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A collection of {@link Statistic} elements
 */
public final class Statistics {

    /**
     * EventProcessor component
     */
    public static final String COMPONENT_EVENT_PROCESSOR = "EventProcessor";
    /**
     * EventHandler component
     */
    public static final String COMPONENT_EVENT_HANDLER = "EventHandler";
    /**
     * EventAssembler component
     */
    public static final String COMPONENT_EVENT_ASSEMBLER = "EventAssembler";
    /**
     * Processing Time statistic
     */
    public static final String STAT_PROCESS_TIME = "processingTime";
    /**
     * Bytes processed statistic
     */
    public static final String STAT_BYTES_PROCESSED = "bytesProcessed";
    /**
     * Event Send Time statistic
     */
    public static final String STAT_EVENT_SEND_TIME = "eventSendTime";
    /**
     * Entity count statistic
     */
    public static final String STAT_ENTITY_COUNT = "entityCount";

    /**
     * Assembly time statistic
     */
    public static final String STAT_ASSEMBLY_TIME = "assemblyTime";

    /**
     * Bytes per second statistic
     */
    public static final String STAT_BYTES_PER_SECOND = "bytesPerSecond";
    /**
     * Parsing time statistic
     */
    public static final String STAT_PARSING_TIME = "parsingTime";
    /**
     * Event processing time statistic
     */
    public static final String STAT_EVENT_PROCESSING_TIME = "eventProcessingTime";
    /**
     * Event count statistic
     */
    public static final String STAT_EVENT_COUNT = "eventCount";
    /**
     * Events sent per second statistic
     */
    public static final String STAT_EVENT_SEND_PER_SECOND = "eventSendPerSecond";
    /**
     * Entities assembled per second statistic
     */
    public static final String STAT_ASSEMBLY_ENTITY_PER_SECOND = "entitiesPerSecond";


    private final Map<String, Statistic<? extends Number>> statisticMap = new LinkedHashMap<>();

    /**
     * Utility method for setting the {@link #STAT_PROCESS_TIME} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setProcessorBytesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PROCESS_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_ASSEMBLY_TIME} linked to the
     * {@link #COMPONENT_EVENT_ASSEMBLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setAssemblyTime(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ASSEMBLY_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_BYTES_PROCESSED} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setBytesProcessed(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_BYTES_PROCESSED, value);
    }

    /**
     * Utility method for setting the {@link #STAT_EVENT_SEND_TIME} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setEventSendTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_EVENT_SEND_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_ENTITY_COUNT} linked to the
     * {@link #COMPONENT_EVENT_ASSEMBLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setEntityCount(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ENTITY_COUNT, value);
    }

    /**
     * Utility method for setting the {@link #STAT_BYTES_PER_SECOND} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setBytesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_BYTES_PER_SECOND, value);
    }

    /**
     * Utility method for setting the {@link #STAT_PARSING_TIME} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setParsingTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PARSING_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_PROCESS_TIME} linked to the
     * {@link #COMPONENT_EVENT_PROCESSOR}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setProcessingTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PROCESS_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_EVENT_PROCESSING_TIME} linked to the
     * {@link #COMPONENT_EVENT_HANDLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setEventProcessingTime(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_PROCESSING_TIME, value);
    }

    /**
     * Utility method for setting the {@link #STAT_ENTITY_COUNT} linked to the
     * {@link #COMPONENT_EVENT_HANDLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setEventCount(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_COUNT, value);
    }

    /**
     * Utility method for setting the {@link #STAT_EVENT_SEND_PER_SECOND} linked to the
     * {@link #COMPONENT_EVENT_HANDLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setEventsSentPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_SEND_PER_SECOND, value);
    }

    /**
     * Utility method for setting the {@link #STAT_ASSEMBLY_ENTITY_PER_SECOND} linked to the
     * {@link #COMPONENT_EVENT_ASSEMBLER}
     *
     * @param value the value
     * @param <T>   the value type
     */
    public <T extends Number> void setAssemblyEntitiesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ASSEMBLY_ENTITY_PER_SECOND, value);
    }

    /**
     * Set a statistic
     *
     * @param componentName the component name
     * @param statisticName the statistic name
     * @param value         the statistic value
     * @param <T>           value type
     */
    public <T extends Number> void setStatistic(String componentName, String statisticName, T value) {
        appendStatistic(new StatisticsRecord<>(componentName, statisticName, value));
    }


    /**
     * Append statistic
     *
     * @param statistic the statistic
     */
    private void appendStatistic(Statistic<? extends Number> statistic) {
        statisticMap.put(statistic.getName(), statistic);
    }

    /**
     * Merge statistics
     *
     * @param statistics another statistics instance
     */
    public void merge(Statistics statistics) {
        statistics.stream().forEach(stat -> statisticMap.putIfAbsent(stat.getName(), stat));
    }

    /**
     * Stream of statistics
     *
     * @return a stream of statistics
     */
    public Stream<Statistic<? extends Number>> stream() {
        return statisticMap.values().stream();
    }

    /**
     * Return a statistic by name
     *
     * @param statisticName the statistic name
     * @return the statistic
     */
    public Optional<Statistic<? extends Number>> getByName(String statisticName) {
        return Optional.ofNullable(statisticMap.getOrDefault(statisticName, null));
    }

    /**
     * Return a list of statistics by component name
     *
     * @param component the component name
     * @return the list of statistics
     */
    public List<Statistic<? extends Number>> getByComponent(String component) {
        return stream().filter(stat -> stat.getComponent().equals(component)).toList();
    }

    /**
     * Convert the statistics to a string output
     *
     * @return the statistics to a string output
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();

        List<String> components = stream().map(Statistic::getComponent).distinct().toList();

        components.forEach(component -> {
            builder.append(component).append(System.lineSeparator());
            getByComponent(component).forEach(statistic -> {
                StringBuilder statisticBuilder = new StringBuilder();
                statisticBuilder.append(padRight(32, statistic.getName()))
                    .append(padLeft(24, formatDecimal(statistic.getValue(), getFraction(statistic.getValue()))))
                    .append(System.lineSeparator());
                builder.append(statisticBuilder.toString().indent(4));
            });
        });

        return builder.toString();
    }

    private int getFraction(Number v) {
        if (v instanceof Integer || v instanceof Long) {
            return 0;
        } else {
            return 8;
        }
    }

    private String padLeft(int padding, String text) {
        return String.format("%" + padding + "s", text);
    }

    private String padRight(int padding, String text) {
        return String.format("%-" + padding + "s", text);
    }

    private String formatDecimal(Number number, int decimals) {
        String decimalString = "";
        if (decimals > 0) {
            decimalString += ".";
            for (int i = 0; i < decimals; i++) {
                decimalString += "0";
            }
        }

        return new DecimalFormat("###,###,###,##0" + decimalString).format(number);
    }

    private record StatisticsRecord<T extends Number>(String component, String name, T value) implements Statistic<T> {

        @Override
        public String getComponent() {
            return this.component();
        }

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public T getValue() {
            return this.value();
        }
    }
}
