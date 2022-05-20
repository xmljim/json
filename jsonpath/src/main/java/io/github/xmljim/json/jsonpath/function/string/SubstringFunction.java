package io.github.xmljim.json.jsonpath.function.string;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.ExpressionArgument;
import io.github.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.github.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SUBSTRING,
        args = {
                @ArgumentDefinition(name = "start", scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class,
                        description = "The start position of the string"
                ),
                @ArgumentDefinition(name = "end", scope = ArgumentScope.OPTIONAL, argType = ExpressionArgument.class,
                        description = "The end index position of the string"
                )
        }
)
public class SubstringFunction extends AbstractJsonPathFunction {
    public SubstringFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.SUBSTRING, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {

        return contextStream
                .filter(context -> context.type() == DataType.STRING)
                .map(context -> {
                    String currentValue = context.value();

                    //ExpressionArgument arg = (ExpressionArgument) getArgument("start").orElseThrow();
                    Number start = getArgumentValue("start", context);

                    if (hasArgument("end")) {
                        //ExpressionArgument endArg = (ExpressionArgument) getArgument("end").get();
                        Number end = getArgumentValue("end", context);
                        currentValue = currentValue.substring(start.intValue(), end.intValue());
                    } else {
                        currentValue = currentValue.substring(start.intValue());
                    }

                    return Context.createSimpleContext(currentValue);
                });
    }
}
