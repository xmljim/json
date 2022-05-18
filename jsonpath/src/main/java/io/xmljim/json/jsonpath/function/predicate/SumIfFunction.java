package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.PredicateArgument;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.util.BuiltIns;
import io.xmljim.json.jsonpath.util.Global;

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
