package io.xmljim.json.jsonpath.function.aggregate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SUM)
public class SumFunction extends AbstractJsonPathFunction {
    public SumFunction(Global global) {
        super(BuiltIns.SUM.functionName(), Collections.emptyList(), global);
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
