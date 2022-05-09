package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionType;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.REPLACE, args = {
    @ArgumentDefinition(name = "match", description = "The string to match", argType = ExpressionArgument.class,
        scope = ArgumentScope.REQUIRED, valueType = ExpressionType.STRING),
    @ArgumentDefinition(name = "replace", description = "The replacement string", argType = ExpressionArgument.class,
        scope = ArgumentScope.REQUIRED, valueType = ExpressionType.STRING),
})
public class ReplaceFunction extends AbstractJsonPathFunction {
    public ReplaceFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.REPLACE.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
            .map(context -> {
                String matchString = getArgumentValue("match", context);
                String replaceString = getArgumentValue("replace", context);

                if (context.type() == NodeType.STRING) {
                    String v = context.value();
                    return Context.createSimpleContext(v.replace(matchString, replaceString));
                } else {
                    return context;
                }
            });
    }
}
