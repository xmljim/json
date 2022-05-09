package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.LOWERCASE)
public class LowerCaseFunction extends AbstractJsonPathFunction {
    public LowerCaseFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.LOWERCASE.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
            .map(context -> {
                if (context.type() == NodeType.STRING) {
                    String v = context.value();
                    return Context.createSimpleContext(v.toLowerCase(Locale.ROOT));
                } else {
                    return context;
                }
            });
    }
}
