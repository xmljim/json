package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

class NullExpression extends SimpleExpression<Void> {
    public NullExpression(String expression, Global global) {
        super(expression, global);
        setValue(null);
    }

    @Override
    public DataType type() {
        return DataType.NULL;
    }
}
