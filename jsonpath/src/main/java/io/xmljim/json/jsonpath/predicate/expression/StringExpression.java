package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.variables.Global;

class StringExpression extends SimpleExpression<String> {
    public StringExpression(String expression, Global global) {
        super(expression, global);
        setValue(expression.substring(1, expression.length() - 1));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.STRING;
    }
}
