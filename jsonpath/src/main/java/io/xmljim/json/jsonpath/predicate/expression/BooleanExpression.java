package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

class BooleanExpression extends SimpleExpression<Boolean> {
    public BooleanExpression(String expression, Global global) {
        super(expression, global);
        setValue(Context.createSimpleContext(Boolean.parseBoolean(expression)));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.BOOLEAN;
    }
}
