package io.xmljim.json.jsonpath.function.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.function.Argument;
import io.xmljim.json.jsonpath.function.ExpressionArgument;
import io.xmljim.json.jsonpath.function.PredicateArgument;
import io.xmljim.json.jsonpath.function.PredicateFunction;
import io.xmljim.json.jsonpath.function.info.ArgumentDefinition;
import io.xmljim.json.jsonpath.function.info.ArgumentScope;
import io.xmljim.json.jsonpath.function.info.FunctionDefinition;
import io.xmljim.json.jsonpath.variables.BuiltIns;

import java.util.List;
import java.util.stream.Stream;

@FunctionDefinition(builtIn = BuiltIns.COUNT_IF, args = {
    @ArgumentDefinition(name = "path", scope = ArgumentScope.REQUIRED, type = ExpressionArgument.class, description = "The path expression of values to evaluate"),
    @ArgumentDefinition(name = "test", scope = ArgumentScope.REQUIRED, type = PredicateArgument.class, description = "The test predicate"),
})
public class CountIfFunction extends AbstractPredicateFunction implements PredicateFunction {
    public CountIfFunction(List<Argument<?, ?>> arguments) {
        super(BuiltIns.COUNT_IF.functionName(), arguments);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        List<Context> theContexts = contextStream.toList();
        ExpressionArgument arg = (ExpressionArgument) getArgument("path").orElseThrow();

        List<Context> contexts = theContexts.stream().flatMap(context -> arg.element().values(context).stream()).toList();

        List<Context> filtered = contexts.stream().filter(getPredicate("test")).toList();
        //long count = contextStream.filter(getPredicate("test")).count();
        return Stream.of(Context.createSimpleContext(filtered.size()));
    }
}
