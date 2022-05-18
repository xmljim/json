package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.ENDS_WITH, args = {
        @ArgumentDefinition(name = "suffix", description = "The string expression to use for evaluating the context value",
                scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, valueType = DataType.STRING),
})
public class EndsWithFunction extends AbstractJsonPathFunction {
    public EndsWithFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.ENDS_WITH, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                .map(context -> {
                    String searchString = getArgumentValue("suffix", context);

                    if (context.type() == DataType.STRING) {
                        String v = context.value();
                        return Context.createSimpleContext(v.endsWith(searchString));
                    } else {
                        return Context.createSimpleContext(false);
                    }
                });
    }
}
