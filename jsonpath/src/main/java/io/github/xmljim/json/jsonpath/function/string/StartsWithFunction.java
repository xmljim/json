package io.github.xmljim.json.jsonpath.function.string;

import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.ExpressionArgument;
import io.github.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.github.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.context.Context;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.STARTS_WITH, args = {
        @ArgumentDefinition(name = "prefix", description = "The string expression to use for evaluating the context value",
                scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, valueType = DataType.STRING),
        @ArgumentDefinition(name = "offset", description = "The character index offset to begin the evaluation",
                scope = ArgumentScope.OPTIONAL, argType = ExpressionArgument.class, valueType = DataType.INTEGER)
})
public class StartsWithFunction extends AbstractJsonPathFunction {
    public StartsWithFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.STARTS_WITH, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                .map(context -> {
                    String searchString = getArgumentValue("prefix", context);
                    Number offset = hasArgument("offset") ? getArgumentValue("offset", context) : 0;

                    if (context.type() == DataType.STRING) {
                        String v = context.value();
                        return Context.createSimpleContext(v.startsWith(searchString, offset.intValue()));
                    } else {
                        return Context.createSimpleContext(false);
                    }
                });
    }
}
