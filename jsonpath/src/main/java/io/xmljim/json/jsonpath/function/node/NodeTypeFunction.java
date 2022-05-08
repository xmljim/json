package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.TYPE)
public class NodeTypeFunction extends AbstractJsonPathFunction {
    public NodeTypeFunction() {
        super(BuiltIns.TYPE.functionName(), Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().name().toLowerCase(Locale.ROOT)));
    }
}
