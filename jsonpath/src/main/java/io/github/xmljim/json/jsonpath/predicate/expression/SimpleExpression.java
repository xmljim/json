package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.context.Context;

import java.util.List;
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

    @Override
    public List<Context> values(Context inputContext) {
        return null;
    }
}
