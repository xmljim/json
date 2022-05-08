package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.IS_PRIMITIVE)
public class IsPrimitiveFunction extends AbstractJsonPathFunction {

    public IsPrimitiveFunction() {
        super(BuiltIns.IS_PRIMITIVE.functionName(), Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().isPrimitive()));
    }
}
