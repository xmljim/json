package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;

class NullExpression extends SimpleExpression<Void> {
    public NullExpression(String expression, Global global) {
        super(expression, global);
        setValue(null);
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.NULL;
    }
}
