package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.factory.model.ElementFactory;
import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonArray;
import io.xmljim.json.service.ServiceManager;

import java.util.Arrays;
import java.util.Optional;

class ListExpression extends AbstractExpression {

    public ListExpression(String expression, Global global) {
        super(expression, global);

        ElementFactory elementFactory = ServiceManager.getProvider(ElementFactory.class);
        JsonArray jsonArray = elementFactory.newArray();

        String[] token = expression.substring(1, expression.length() - 1).split(",");
        Arrays.stream(token).forEach(item -> {
            if (item.strip().matches(Expression.STRING_PATTERN)) {
                jsonArray.add(item.strip().substring(1, item.strip().length() - 1));
            } else if (item.strip().matches(Expression.NUMBER_PATTERN)) {
                jsonArray.add(parseNumber(item.strip()));
            } else if (item.strip().matches(Expression.BOOLEAN_PATTERN)) {
                jsonArray.add(Boolean.parseBoolean(item.strip()));
            } else if (item.strip().matches(Expression.NULL_PATTERN)) {
                jsonArray.add(null);
            }
        });

        set(Context.create(jsonArray));
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        return Optional.of(values().get(index));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getValue(Context inputContext) {
        Optional<Context> context = getContext(inputContext);
        return context.flatMap(value -> (Optional<T>) Optional.of(value.get()));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.LIST;
    }

    private Number parseNumber(String expression) {
        String regexIntOrLong = "[-]?\\d+";
        String regexDouble = "[-]?\\d+\\.\\d+";
        Number value = null;

        if (expression.matches(regexIntOrLong)) {
            long longValue = Long.parseLong(expression);

            if (longValue < Integer.MAX_VALUE) {
                value = ((Long) longValue).intValue();
            } else {
                value = longValue;
            }
        } else if (expression.matches(regexDouble)) {
            value = Double.parseDouble(expression);
        }

        return value;
    }
}