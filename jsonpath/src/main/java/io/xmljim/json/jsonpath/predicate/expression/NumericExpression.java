package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.util.DataType;
import io.xmljim.json.jsonpath.util.Global;

class NumericExpression extends SimpleExpression<Number> {
    private DataType type;

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
                type = DataType.INTEGER;
            } else {
                value = longValue;
                type = DataType.LONG;
            }
        } else if (expression.matches(regexDouble)) {
            value = Double.parseDouble(expression);
            type = DataType.DOUBLE;
        }

        return value;
    }

    @Override
    public DataType type() {
        return type;
    }
}
