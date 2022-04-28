package io.xmljim.json.factory.parser;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class Statistics {

    public static final String COMPONENT_EVENT_PROCESSOR = "EventProcessor";
    public static final String COMPONENT_EVENT_HANDLER = "EventHandler";
    public static final String COMPONENT_EVENT_ASSEMBLER = "EventAssembler";
    public static final String STAT_PROCESS_TIME = "processingTime";
    public static final String STAT_BYTES_PROCESSED = "bytesProcessed";
    public static final String STAT_EVENT_SEND_TIME = "eventSendTime";
    public static final String STAT_ENTITY_COUNT = "entityCount";
    public static final String STAT_ASSEMBLY_TIME = "assemblyTime";
    public static final String STAT_BYTES_PER_SECOND = "bytesPerSecond";
    public static final String STAT_PARSING_TIME = "parsingTime";
    public static final String STAT_EVENT_PROCESSING_TIME = "eventProcessingTime";
    public static final String STAT_EVENT_COUNT = "eventCount";
    public static final String STAT_EVENT_SEND_PER_SECOND = "eventSendPerSecond";
    public static final String STAT_ASSEMBLY_ENTITY_PER_SECOND = "entitiesPerSecond";


    private final Map<String, Statistic<? extends Number>> statisticMap = new LinkedHashMap<>();

    public <T extends Number> void setProcessorBytesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PROCESS_TIME, value);
    }

    public <T extends Number> void setAssemblyTime(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ASSEMBLY_TIME, value);
    }

    public <T extends Number> void setBytesProcessed(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_BYTES_PROCESSED, value);
    }

    public <T extends Number> void setEventSendTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_EVENT_SEND_TIME, value);
    }

    public <T extends Number> void setEntityCount(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ENTITY_COUNT, value);
    }

    public <T extends Number> void setBytesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_BYTES_PER_SECOND, value);
    }

    public <T extends Number> void setParsingTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PARSING_TIME, value);
    }

    public <T extends Number> void setProcessingTime(T value) {
        setStatistic(COMPONENT_EVENT_PROCESSOR, STAT_PROCESS_TIME, value);
    }

    public <T extends Number> void setEventProcessingTime(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_PROCESSING_TIME, value);
    }

    public <T extends Number> void setEventCount(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_COUNT, value);
    }

    public <T extends Number> void setEventsSentPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_HANDLER, STAT_EVENT_SEND_PER_SECOND, value);
    }

    public <T extends Number> void setAssemblyEntitiesPerSecond(T value) {
        setStatistic(COMPONENT_EVENT_ASSEMBLER, STAT_ASSEMBLY_ENTITY_PER_SECOND, value);
    }

    public <T extends Number> void setStatistic(String componentName, String statisticName, T value) {
        appendStatistic(new StatisticsRecord<>(componentName, statisticName, value));
    }


    private void appendStatistic(Statistic<? extends Number> statistic) {
        statisticMap.put(statistic.getName(), statistic);
    }

    public void merge(Statistics statistics) {
        statistics.stream().forEach(stat -> {
            statisticMap.putIfAbsent(stat.getName(), stat);
        });
    }

    public Stream<Statistic<? extends Number>> stream() {
        return statisticMap.values().stream();
    }

    public Optional<Statistic<? extends Number>> getByName(String statisticName) {
        return Optional.ofNullable(statisticMap.getOrDefault(statisticName, null));
    }

    public List<Statistic<? extends Number>> getByComponent(String component) {
        return stream().filter(stat -> stat.getComponent().equals(component)).toList();
    }

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
