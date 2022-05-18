package io.xmljim.json.jsonpath.filter;

import io.xmljim.json.jsonpath.util.Global;

abstract class AbstractFilter implements Filter {

    private final String expression;
    private final FilterType filterType;
    private final Global global;

    public AbstractFilter(String expression, FilterType filterType, Global global) {
        this.expression = expression;
        this.filterType = filterType;
        this.global = global;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public FilterType getFilterType() {
        return filterType;
    }

    public Global getGlobal() {
        return global;
    }

    public String toString() {
        return expression;
    }


}
