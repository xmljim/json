package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.function.info.ArgumentInfo;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.jsonpath.variables.Global;

abstract class Arguments {
    public static Argument<?, ?> newArgument(String expression, ArgumentInfo info, Global global) {
        if (info.type().isAssignableFrom(ExpressionArgument.class)) {
            return newExpressionArgument(expression, info, global);
        }

        return null;
    }

    public static ExpressionArgument newExpressionArgument(String expression, ArgumentInfo info, Global global) {
        return new ExpressionArgumentImpl(info.name(), ExpressionFactory.create(expression, global));
    }
}
