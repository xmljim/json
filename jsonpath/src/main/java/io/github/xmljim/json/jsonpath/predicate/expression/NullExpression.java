package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.DataType;

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
