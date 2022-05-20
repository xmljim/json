package io.github.xmljim.json.jsonpath.function.aggregate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.AVERAGE)
public class AvgFunction extends AbstractJsonPathFunction {
    public AvgFunction(Global global) {
        super(BuiltIns.AVERAGE.functionName(), Collections.emptyList(), global);
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
