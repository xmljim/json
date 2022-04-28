package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

class NullExpression extends SimpleExpression<Void> {
    public NullExpression(String expression, Global global) {
        super(expression, global);
        setValue(Context.createSimpleContext(null));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.NULL;
    }
}
