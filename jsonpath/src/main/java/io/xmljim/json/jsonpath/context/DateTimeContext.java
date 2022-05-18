package io.xmljim.json.jsonpath.context;

import io.xmljim.json.jsonpath.util.DataType;

import java.time.LocalDateTime;

public class DateTimeContext extends TemporalContext<LocalDateTime> {
    public DateTimeContext(LocalDateTime value) {
        super(value);
    }

    @Override
    public DataType type() {
        return DataType.DATETIME;
    }
}
