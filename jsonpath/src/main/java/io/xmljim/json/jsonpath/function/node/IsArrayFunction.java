package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.IS_ARRAY)
public class IsArrayFunction extends AbstractJsonPathFunction {
    public IsArrayFunction(Global global) {
        super(BuiltIns.IS_ARRAY, Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().isArray()));
    }
}
