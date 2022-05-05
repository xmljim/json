package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.predicate.expression.Expression;

class ExpressionArgumentImpl extends AbstractArgument<Expression, Context> implements ExpressionArgument {

    public ExpressionArgumentImpl(String name, Expression element) {
        super(name, element);
    }

    @Override
    public Context apply(Context context) {
        return element().getContext(context).orElse(null);
    }
}
