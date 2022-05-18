package io.xmljim.json.jsonpath.function.node;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

import java.util.Collections;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.PARENT)
public class ParentFunction extends AbstractJsonPathFunction {
    public ParentFunction(Global global) {
        super(BuiltIns.PARENT, Collections.emptyList(), global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> {
            return context.getParent().orElse(Context.defaultContext());
        });
    }
}
