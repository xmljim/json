package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.Properties;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateExpression extends SimpleExpression<LocalDate> implements TemporalExpression {
    public DateExpression(String expression, Global global) {
        super(expression, global);
        set(Context.createTemporalContext(parseDate(expression, global)));
    }

    private LocalDate parseDate(String expression, Global global) {
        if (global.getProperty(Properties.KEY_DATE_PARSE_FROM_LONG)) {
            return Instant.ofEpochMilli(Integer.parseInt(expression)).atZone(ZoneId.systemDefault()).toLocalDate();
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(global.getProperty(Properties.KEY_DATE_FORMAT));
        return LocalDate.parse(expression, dateTimeFormatter);
    }

    @Override
    public DataType type() {
        return DataType.DATE;
    }
}
