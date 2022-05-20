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
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.SUM_IF, args = {
        @ArgumentDefinition(name = "list", scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, description = "The list of values to evaluate"),
        @ArgumentDefinition(name = "test", scope = ArgumentScope.REQUIRED, argType = PredicateArgument.class, description = "The test predicate"),
        @ArgumentDefinition(name = "valueExpr", scope = ArgumentScope.REQUIRED, argType = ExpressionArgument.class, description = "The expression containing the value to sum"),
})
public class SumIfFunction extends AbstractPredicateFunction {
    public SumIfFunction(List<Argument<?, ?>> arguments, Global global) {
        super(BuiltIns.SUM_IF, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        Expression expression = getExpression("list");
        Stream<Context> nestedContextStream = contextStream.flatMap(context -> expression.values(context).stream());
        Stream<Context> filteredStream = nestedContextStream.filter(getPredicate("test"));

        double result = filteredStream.flatMap(context -> getExpression("valueExpr").values(context).stream())
                .filter(context -> context.type().isNumeric())
                .mapToDouble(Context::value)
                .summaryStatistics()
                .getSum();

        return Stream.of(Context.createSimpleContext(result));
    }
}
