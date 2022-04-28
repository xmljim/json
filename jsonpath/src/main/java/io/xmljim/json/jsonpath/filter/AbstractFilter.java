package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.Global;

abstract class AbstractFilter implements Filter {

    private final String expression;
    private final FilterType operatorType;
    private final Global global;

    public AbstractFilter(String expression, FilterType operatorType, Global global) {
        this.expression = expression;
        this.operatorType = operatorType;
        this.global = global;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public FilterType getOperatorType() {
        return operatorType;
    }

    public Global getGlobal() {
        return global;
    }

    public String toString() {
        return expression;
    }


}
