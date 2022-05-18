package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.TRIM)
public class TrimFunction extends AbstractJsonPathFunction {

    public TrimFunction(List<Argument<?, ?>> argumentList, Global global) {
        super(BuiltIns.TRIM, argumentList, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                .map(context -> {
                    if (context.type() == DataType.STRING) {
                        String v = context.value();
                        return Context.createSimpleContext(v.strip());
                    } else {
                        return context;
                    }
                });
    }
}
