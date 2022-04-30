package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;

class NumericExpression extends SimpleExpression<Number> {
    private ExpressionType type;

    public NumericExpression(String expression, Global global) {
        super(expression, global);
        setValue(parseNumber(expression));
    }

    private Number parseNumber(String expression) {
        String regexIntOrLong = "[-]?\\d+";
        String regexDouble = "[-]?\\d+\\.\\d+";
        Number value = null;

        if (expression.matches(regexIntOrLong)) {
            long longValue = Long.parseLong(expression);

            if (longValue < Integer.MAX_VALUE) {
                value = ((Long) longValue).intValue();
                type = ExpressionType.INTEGER;
            } else {
                value = longValue;
                type = ExpressionType.LONG;
            }
        } else if (expression.matches(regexDouble)) {
            value = Double.parseDouble(expression);
            type = ExpressionType.DOUBLE;
        }

        return value;
    }

    @Override
    public ExpressionType type() {
        return type;
    }
}
