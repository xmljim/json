package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.Properties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeExpression extends SimpleExpression<LocalDateTime> implements TemporalExpression {
    public DateTimeExpression(String expression, Global global) {
        super(expression, global);
        set(Context.createTemporalContext(parseDate(expression, global)));
    }

    private LocalDateTime parseDate(String expression, Global global) {
        if (global.getProperty(Properties.KEY_DATE_TIME_PARSE_FROM_LONG)) {
            return Instant.ofEpochMilli(Integer.parseInt(expression)).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(global.getProperty(Properties.KEY_DATE_TIME_FORMAT));
        return LocalDateTime.parse(expression, dateTimeFormatter);
    }

    @Override
    public DataType type() {
        return DataType.DATETIME;
    }
}
