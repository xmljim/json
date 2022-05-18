package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

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
