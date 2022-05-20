package io.github.xmljim.json.jsonpath.function;

import io.github.xmljim.json.jsonpath.function.info.ArgumentInfo;
import io.github.xmljim.json.jsonpath.compiler.Compiler;
import io.github.xmljim.json.jsonpath.context.Context;
import io.github.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.github.xmljim.json.jsonpath.util.Global;

import java.util.function.Predicate;

abstract class Arguments {
    public static Argument<?, ?> newArgument(String expression, ArgumentInfo info, Global global) {
        if (info.type().isAssignableFrom(ExpressionArgument.class)) {
            return newExpressionArgument(expression, info, global);
        } else if (info.type().isAssignableFrom(PredicateArgument.class)) {
            return newPredicateExpressionArgument(expression, info, global);
        }
        //todo: consider throwing exception.
        return null;
    }

    public static ExpressionArgument newExpressionArgument(String expression, ArgumentInfo info, Global global) {
        return new ExpressionArgumentImpl(info.name(), ExpressionFactory.create(expression, global));
    }

    public static PredicateArgument newPredicateExpressionArgument(String expression, ArgumentInfo info, Global global) {
        Predicate<Context> predicate = Compiler.newPredicateCompiler(expression, global).compile();
        return new PredicateArgumentImpl(info.name(), predicate);
    }
}
