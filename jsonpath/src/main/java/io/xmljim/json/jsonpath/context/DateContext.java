package io.xmljim.json.jsonpath.context;

import io.xmljim.json.jsonpath.util.DataType;

import java.time.LocalDate;

public class DateContext extends TemporalContext<LocalDate> {
    public DateContext(LocalDate value) {
        super(value);
    }

    @Override
    public DataType type() {
        return DataType.DATE;
    }
}
