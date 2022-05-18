package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.util.DataType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface TemporalExpression extends Expression {

    default long convertToLong(Context context) {
        if (getContextType(context) == DataType.DATE) {
            LocalDate localDate = get(context);
            if (localDate != null) {
                Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                return instant.toEpochMilli();
            }
            return 0;
        } else if (getContextType(context) == DataType.DATETIME) {
            LocalDateTime localDateTime = get(context);
            if (localDateTime != null) {
                Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                return instant.toEpochMilli();
            }
            return 0;
        }

        return 0;
    }
}
