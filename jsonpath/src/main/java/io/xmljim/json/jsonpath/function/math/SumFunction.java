package io.xmljim.json.jsonpath.function.math;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(name = "sum", description = "returns the sum of all numeric values in context")
public class SumFunction extends AbstractJsonPathFunction {
    public SumFunction() {
        super("sum", Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        double value = contextStream.filter(context -> context.type().isNumeric())
            .mapToDouble(Context::value)
            .summaryStatistics()
            .getSum();

        return Stream.of(Context.createSimpleContext(value));
    }
}
