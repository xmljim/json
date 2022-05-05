package io.xmljim.json.jsonpath.function.math;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(name = "average", description = "returns the average of all numeric values in context")
public class AvgFunction extends AbstractJsonPathFunction {
    public AvgFunction() {
        super("average", Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        double value = contextStream.filter(context -> context.type().isNumeric())
            .mapToDouble(Context::value)
            .summaryStatistics()
            .getAverage();

        return Stream.of(Context.createSimpleContext(value));
    }
}
