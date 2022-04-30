package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

import java.util.Optional;

abstract class SimpleExpression<T> extends AbstractExpression {
    private Context value;


    public SimpleExpression(String expression, Global global) {
        super(expression, global);

    }

    public void setValue(T value) {
        set(Context.createSimpleContext(value));
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        try {
            return Optional.ofNullable(values().get(index));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
