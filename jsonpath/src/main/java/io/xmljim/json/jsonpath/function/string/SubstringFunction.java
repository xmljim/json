package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.model.NodeType;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(name = "substring", description = "Returns a substring of a string value in context",
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
        super("substring", arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {

        return contextStream
            .filter(context -> context.type() == NodeType.STRING)
            .map(context -> {
                String currentValue = context.value();

                ExpressionArgument arg = (ExpressionArgument) getArgument("start").orElseThrow();
                int start = arg.apply(context).value();

                if (getArgument("end").isPresent()) {
                    ExpressionArgument endArg = (ExpressionArgument) getArgument("end").get();
                    int end = endArg.apply(context).value();
                    currentValue = currentValue.substring(start, end);
                } else {
                    currentValue = currentValue.substring(start);
                }

                return Context.createSimpleContext(currentValue);
            });
    }
}
