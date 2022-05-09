package io.xmljim.json.jsonpath.function.string;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionType;
import io.xmljim.json.jsonpath.variables.BuiltIns;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.model.NodeType;
import io.xmljim.json.service.ServiceManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SPLIT, args = {
    @ArgumentDefinition(name = "regex", description = "The expression to match for splitting", argType = ExpressionArgument.class,
        scope = ArgumentScope.REQUIRED, valueType = ExpressionType.REGEX)
})
public class SplitFunction extends AbstractJsonPathFunction {
    public SplitFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.STARTS_WITH.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        return contextStream
            .map(context -> {
                String searchString = getArgumentValue("regex", context);
                JsonArray values = ServiceManager.getProvider(ElementFactory.class).newArray();
                if (context.type() == NodeType.STRING) {
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
