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

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.CONCAT,
    args = {@ArgumentDefinition(name = "expr", scope = ArgumentScope.VARARGS, argType = ExpressionArgument.class,
        description = "The expression value", valueType = ExpressionType.STRING)}
)
public class ConcatFunction extends AbstractJsonPathFunction {
    public ConcatFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.CONCAT.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream.map(context -> {
            StringBuilder builder = new StringBuilder();
            if (hasArgument("expr")) {
                getArgumentValueStream("expr", context).forEach(builder::append);
            }
            return Context.createSimpleContext(builder.toString());
        });
    }
}
