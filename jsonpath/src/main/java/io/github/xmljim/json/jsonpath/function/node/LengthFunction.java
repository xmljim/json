package io.github.xmljim.json.jsonpath.function.node;

import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.LENGTH)
public class LengthFunction extends AbstractJsonPathFunction {

    public LengthFunction(Global global) {
        super(BuiltIns.LENGTH.functionName(), Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> {
            int length = switch (context.type()) {
                case ARRAY -> context.array().size();
                case OBJECT -> context.object().size();
                case STRING -> String.valueOf(context.value()).length();
                default -> -1;
            };
            return Context.createSimpleContext(length);
        });
    }
}
