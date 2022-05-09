package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.PredicateArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionType;
import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.COUNT_IF, args = {
    @ArgumentDefinition(name = "list", scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, valueType = ExpressionType.LIST,
        description = "The list of values to evaluate"),
    @ArgumentDefinition(name = "test", scope = ArgumentScope.REQUIRED, argType = PredicateArgument.class, valueType = ExpressionType.BOOLEAN,
        description = "The test predicate"),
})
public class CountIfFunction extends AbstractPredicateFunction {
    public CountIfFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.COUNT_IF.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        Expression expression = getExpression("list");
        Stream<Context> nestedContextStream = contextStream.flatMap(context -> expression.values(context).stream());
        Stream<Context> filteredStream = nestedContextStream.filter(getPredicate("test"));
        return Stream.of(Context.createSimpleContext(filteredStream.count()));
    }
}
