package io.xmljim.json.jsonpath.function.aggregate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.COUNT)
public class CountFunction extends AbstractJsonPathFunction {
    public CountFunction(Global global) {
        super(BuiltIns.COUNT.functionName(), Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return Stream.of(Context.createSimpleContext(contextStream.count()));
    }
}
