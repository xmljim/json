package io.github.xmljim.json.jsonpath.function.date;

import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.function.AbstractJsonPathFunction;
import io.github.xmljim.json.jsonpath.function.Argument;
import io.github.xmljim.json.jsonpath.predicate.expression.Expression;
import io.github.xmljim.json.jsonpath.util.BuiltIns;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.List;
import java.util.stream.Stream;

public class DateOfFunction extends AbstractJsonPathFunction {
    public DateOfFunction(BuiltIns builtIn, List<Argument<?, ?>> arguments, Global global) {
        super(builtIn, arguments, global);
    }

    @Override
    public Stream<Context> apply(Stream<Context> contextStream) {
        Expression dateExpression = getExpression("dateExpr");
        Stream<Context> nestedContextStream = contextStream.flatMap(context -> dateExpression.values(context).stream());
        ToDateFunction toDateFunction = new ToDateFunction(arguments(), getGlobal());
        return toDateFunction.apply(nestedContextStream);
    }
}
