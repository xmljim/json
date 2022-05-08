package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SUBSTRING,
    args = {
        @ArgumentDefinition(name = "start", scope = ArgumentScope.REQUIRED, type = ExpressionArgument.class,
            description = "The start position of the string"
        ),
        @ArgumentDefinition(name = "end", scope = ArgumentScope.OPTIONAL, type = ExpressionArgument.class,
            description = "The end index position of the string"
        )
    }
)
public class SubstringFunction extends AbstractJsonPathFunction {
    public SubstringFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.SUBSTRING.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {

        return contextStream
                   .filter(context -> context.type() == NodeType.STRING)
                   .map(context -> {
                       String currentValue = context.value();

                       //ExpressionArgument arg = (ExpressionArgument) getArgument("start").orElseThrow();
                       int start = getArgumentValue("start", context);

                       if (hasArgument("end")) {
                           //ExpressionArgument endArg = (ExpressionArgument) getArgument("end").get();
                           int end = getArgumentValue("end", context);
                           currentValue = currentValue.substring(start, end);
                       } else {
                           currentValue = currentValue.substring(start);
                       }

                       return Context.createSimpleContext(currentValue);
                   });
    }
}
