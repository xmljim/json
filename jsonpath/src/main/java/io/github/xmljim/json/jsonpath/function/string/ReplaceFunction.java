package io.github.xmljim.json.jsonpath.function.string;

import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.ExpressionArgument;
import io.github.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.github.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.util.DataType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.REPLACE, args = {
        @ArgumentDefinition(name = "match", description = "The string to match", argType = ExpressionArgument.class,
                scope = ArgumentScope.REQUIRED, valueType = DataType.STRING),
        @ArgumentDefinition(name = "replace", description = "The replacement string", argType = ExpressionArgument.class,
                scope = ArgumentScope.REQUIRED, valueType = DataType.STRING),
})
public class ReplaceFunction extends AbstractJsonPathFunction {
    public ReplaceFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.REPLACE, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
                .map(context -> {
                    String matchString = getArgumentValue("match", context);
                    String replaceString = getArgumentValue("replace", context);

                    if (context.type() == DataType.STRING) {
                        String v = context.value();
                        return Context.createSimpleContext(v.replace(matchString, replaceString));
                    } else {
                        return context;
                    }
                });
    }
}
