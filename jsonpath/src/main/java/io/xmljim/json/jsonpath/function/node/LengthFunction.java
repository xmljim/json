package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.LENGTH)
public class LengthFunction extends AbstractJsonPathFunction {

    public LengthFunction() {
        super(BuiltIns.LENGTH.functionName(), Collections.emptyList());
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
