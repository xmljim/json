package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

abstract class AbstractExpression implements PredicateExpression {
    private final String expression;
    private Context value;
    private final Global global;
    private List<Context> values = new ArrayList<>();

    public AbstractExpression(String expression, Global global) {
        this.expression = expression;
        this.global = global;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public void set(Context value) {
        this.values.add(value);
    }

    public void set(Optional<Context> value) {
        value.ifPresent(this::set);
    }

    public void set(List<Context> values) {
        this.values = values;
    }

    public void set(Stream<Context> valueStream) {
        set(valueStream.toList());
    }

    public Global getGlobal() {
        return global;
    }

    @Override
    public int size(Context inputContext) {
        return values.size();
    }

    public List<Context> values() {
        return values;
    }

    public String toString() {
        return expression;
    }

}
