package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.variables.Global;

import java.util.regex.Pattern;

class RegexExpression extends SimpleExpression<String> {
    private Pattern pattern;

    public RegexExpression(String expression, Global global) {
        super(expression, global);
        setValue(expression);
    }


    @Override
    public ExpressionType type() {
        return ExpressionType.REGEX;
    }
}
