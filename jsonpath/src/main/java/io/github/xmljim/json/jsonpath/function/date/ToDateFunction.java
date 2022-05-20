package io.github.xmljim.json.jsonpath.function.date;

import io.github.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.github.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.ExpressionArgument;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.Properties;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.TO_DATE, args = {
    @ArgumentDefinition(name = "format", scope = ArgumentScope.OPTIONAL, argType = ExpressionArgument.class,
        valueType = DataType.STRING, description = "A valid date format to use for parsing the date string")
})
public class ToDateFunction extends AbstractJsonPathFunction {
    public ToDateFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.TO_DATE, arguments, global);
    }


    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> {
            if (context.type() == DataType.STRING) {
                String dateFormat = hasArgument("format") ? getArgumentValue("format", context) :
                    getGlobal().getProperty(Properties.KEY_DATE_FORMAT);
                String dateValue = context.value();
                LocalDate localDate = LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(dateFormat));
                return Context.createTemporalContext(localDate);
            } else if (context.type().isNumeric()) {
                Number number = context.value();
                return Context.createTemporalContext(LocalDate.ofInstant(Instant.ofEpochMilli(number.longValue()), ZoneId.systemDefault()));
            } else {
                return Context.defaultContext();
            }
        });
    }
}
