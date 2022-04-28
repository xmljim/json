package io.xmljim.json.jsonpath;

import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

public interface Global {
    PredicateExpression getVariable(String name);
}
