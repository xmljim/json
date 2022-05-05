package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(name = "is-primitive", description = "returns if the node type for each context element is a primitive type")
public class IsPrimitiveFunction extends AbstractJsonPathFunction {

    public IsPrimitiveFunction() {
        super("is-primitive", Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().isPrimitive()));
    }
}
