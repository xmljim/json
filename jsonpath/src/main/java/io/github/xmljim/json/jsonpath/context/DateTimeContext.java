package io.github.xmljim.json.jsonpath.context;

import io.github.xmljim.json.jsonpath.util.DataType;

import java.time.LocalDateTime;

class DateTimeContext extends TemporalContext<LocalDateTime> {
    public DateTimeContext(LocalDateTime value) {
        super(value);
    }

    @Override
    public DataType type() {
        return DataType.DATETIME;
    }
}
