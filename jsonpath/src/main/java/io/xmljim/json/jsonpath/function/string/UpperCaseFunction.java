package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.NodeType;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.UPPERCASE)
public class UpperCaseFunction extends AbstractJsonPathFunction {
    public UpperCaseFunction() {
        super(BuiltIns.UPPERCASE.functionName(), Collections.emptyList());
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                   .map(context -> {
                       if (context.type() == NodeType.STRING) {
                           String v = context.value();
                           return Context.createSimpleContext(v.toUpperCase(Locale.ROOT));
                       } else {
                           return context;
                       }
                   });
    }
}
