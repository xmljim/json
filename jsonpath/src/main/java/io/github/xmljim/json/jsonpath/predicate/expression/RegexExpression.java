package io.github.xmljim.json.jsonpath.predicate.expression;

import io.github.xmljim.json.jsonpath.util.Global;
import io.github.xmljim.json.jsonpath.util.DataType;

import java.util.regex.Pattern;

class RegexExpression extends SimpleExpression<String> {
    private Pattern pattern;

    public RegexExpression(String expression, Global global) {
        super(expression, global);
        setValue(expression);
    }

    @Override
    public DataType type() {
        return DataType.REGEX;
    }
}
