package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;

public interface PredicateExpression {
    <T> T getValue(Context inputContext);

    Context get(Context inputContext);

    ExpressionType type();
}
