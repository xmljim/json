package io.xmljim.json.jsonpath;

import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;
import io.xmljim.json.model.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Variables implements Global {
    private final Map<String, PredicateExpression> variables = new HashMap<>();

    public void setVariable(String name, PredicateExpression expression) {
        variables.putIfAbsent(name, expression);
    }

    public void setVariable(String name, Number number) {
        setVariable(name, Expression.create(number.toString(), this));
    }

    public void setVariable(String name, String stringValue) {
        setVariable(name, Expression.create("'" + stringValue + "'", this));
    }

    public void setVariable(String name, Boolean booleanValue) {
        setVariable(name, Expression.create(booleanValue.toString(), this));
    }

    public void setVariable(String name, JsonNode node) {
        setVariable(name, Expression.withJsonNode(node, this));
    }

    @Override
    public PredicateExpression getVariable(String name) {
        return variables.getOrDefault(name, Expression.create("null", this));
    }
}
