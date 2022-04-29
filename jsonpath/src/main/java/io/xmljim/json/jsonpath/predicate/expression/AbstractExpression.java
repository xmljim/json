package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;

abstract class AbstractExpression implements PredicateExpression {
    private final String expression;
    private Context value;
    private final Global global;

    public AbstractExpression(String expression, Global global) {
        this.expression = expression;
        this.global = global;
    }

    public String getExpression() {
        return expression;
    }

    private void set(Context value) {
        this.value = value;
    }

    public Global getGlobal() {
        return global;
    }

    public String toString() {
        return expression;
    }

}
