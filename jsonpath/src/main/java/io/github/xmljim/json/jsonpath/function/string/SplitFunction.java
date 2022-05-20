package io.github.xmljim.json.jsonpath.function.string;

import io.github.xmljim.json.factory.model.ElementFactory;
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
import io.github.xmljim.json.model.JsonArray;
import io.github.xmljim.json.service.ServiceManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SPLIT, args = {
    @ArgumentDefinition(name = "regex", description = "The expression to match for splitting", argType = ExpressionArgument.class,
        scope = ArgumentScope.REQUIRED, valueType = DataType.REGEX)
})
public class SplitFunction extends AbstractJsonPathFunction {
    public SplitFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.STARTS_WITH, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
            .map(context -> {
                String searchString = getArgumentValue("regex", context);
                JsonArray values = ServiceManager.getProvider(ElementFactory.class).newArray();
                if (context.type() == DataType.STRING) {
                    String v = context.value();
                    String[] results = v.split(searchString);
                    Arrays.stream(results).forEach(values::add);
                } else {
                    values.add(context.get());
                }

                return Context.createSimpleContext(values);
            });
    }
}
