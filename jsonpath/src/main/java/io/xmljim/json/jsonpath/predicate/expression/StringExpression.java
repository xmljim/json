package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

class StringExpression extends SimpleExpression<String> {
    public StringExpression(String expression, Global global) {
        super(expression, global);
        setValue(Context.createSimpleContext(expression.substring(1, expression.length() - 1)));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.STRING;
    }
}
