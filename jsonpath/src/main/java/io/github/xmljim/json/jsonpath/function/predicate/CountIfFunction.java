package io.github.xmljim.json.jsonpath.function.predicate;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.function.ExpressionArgument;
import io.github.xmljim.json.jsonpath.function.PredicateArgument;
import io.github.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.github.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.github.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.DataType;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.COUNT_IF, args = {
        @ArgumentDefinition(name = "list", scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, valueType = DataType.LIST,
                description = "The list of values to evaluate"),
        @ArgumentDefinition(name = "test", scope = ArgumentScope.REQUIRED, argType = PredicateArgument.class, valueType = DataType.BOOLEAN,
                description = "The test predicate"),
})
public class CountIfFunction extends AbstractPredicateFunction {
    public CountIfFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.COUNT_IF, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        Expression expression = getExpression("list");
        Stream<Context> nestedContextStream = contextStream.flatMap(context -> expression.values(context).stream());
        Stream<Context> filteredStream = nestedContextStream.filter(getPredicate("test"));
        return Stream.of(Context.createSimpleContext(filteredStream.count()));
    }
}
