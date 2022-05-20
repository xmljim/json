package io.github.xmljim.json.jsonpath.function.node;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.model.NodeType;

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
