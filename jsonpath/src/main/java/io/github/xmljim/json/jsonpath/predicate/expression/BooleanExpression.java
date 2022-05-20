package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.DataType;

class BooleanExpression extends SimpleExpression<Boolean> {
    public BooleanExpression(String expression, Global global) {
        super(expression, global);
        setValue(Boolean.parseBoolean(expression));
    }

    @Override
    public DataType type() {
        return DataType.BOOLEAN;
    }
}
