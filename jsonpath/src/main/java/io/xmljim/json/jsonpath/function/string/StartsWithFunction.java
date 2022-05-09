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

@FunctionDefinition(builtIn = BuiltIns.STARTS_WITH, args = {
    @ArgumentDefinition(name = "prefix", description = "The string expression to use for evaluating the context value",
        scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, valueType = ExpressionType.STRING),
    @ArgumentDefinition(name = "offset", description = "The character index offset to begin the evaluation",
        scope = ArgumentScope.OPTIONAL, argType = ExpressionArgument.class, valueType = ExpressionType.INTEGER)
})
public class StartsWithFunction extends AbstractJsonPathFunction {
    public StartsWithFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.STARTS_WITH.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {


        return contextStream
            .map(context -> {
                String searchString = getArgumentValue("prefix", context);
                Number offset = hasArgument("offset") ? getArgumentValue("offset", context) : 0;

                if (context.type() == NodeType.STRING) {
                    String v = context.value();
                    return Context.createSimpleContext(v.startsWith(searchString, offset.intValue()));
                } else {
                    return Context.createSimpleContext(false);
                }
            });
    }
}
