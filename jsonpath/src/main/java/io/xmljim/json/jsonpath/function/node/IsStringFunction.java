package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.IS_STRING)
public class IsStringFunction extends AbstractJsonPathFunction {
    public IsStringFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.IS_STRING.functionName(), arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> Context.createSimpleContext(context.type().equals(NodeType.STRING)));
    }
}
