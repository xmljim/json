package io.github.xmljim.json.jsonpath.context;

import io.github.xmljim.json.jsonpath.util.DataType;

import java.time.LocalDate;

class DateContext extends TemporalContext<LocalDate> {
    public DateContext(LocalDate value) {
        super(value);
    }

    @Override
    public DataType type() {
        return DataType.DATE;
    }
}
