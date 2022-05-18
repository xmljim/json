package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.UPPERCASE)
public class UpperCaseFunction extends AbstractJsonPathFunction {
    public UpperCaseFunction(Global global) {
        super(BuiltIns.UPPERCASE, Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                .map(context -> {
                    if (context.type() == DataType.STRING) {
                        String v = context.value();
                        return Context.createSimpleContext(v.toUpperCase(Locale.ROOT));
                    } else {
                        return context;
                    }
                });
    }
}
