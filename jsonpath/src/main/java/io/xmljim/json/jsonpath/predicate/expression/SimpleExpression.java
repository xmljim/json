package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

abstract class SimpleExpression<T> extends AbstractExpression {
    private Context value;


    public SimpleExpression(String expression, Global global) {
        super(expression, global);

    }

    public void setValue(Context value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue(Context inputContext) {
        return (T) get(inputContext).get().asJsonValue().get();
    }

    @Override
    public Context get(Context inputContext) {
        return value;
    }
}
