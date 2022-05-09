package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.TRIM)
public class TrimFunction extends AbstractJsonPathFunction {

    public TrimFunction(List<Argument<?, ?>> argumentList) {
        super(BuiltIns.TRIM.functionName(), argumentList);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
            .map(context -> {
                if (context.type() == NodeType.STRING) {
                    String v = context.value();
                    return Context.createSimpleContext(v.strip());
                } else {
                    return context;
                }
            });
    }
}
