package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(name = "is-numeric", description = "Evaluates whether the context element is a numeric value")
public class IsNumericFunction extends AbstractJsonPathFunction {
    public IsNumericFunction() {
        super("is-numeric", Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().isNumeric()));
    }
}
