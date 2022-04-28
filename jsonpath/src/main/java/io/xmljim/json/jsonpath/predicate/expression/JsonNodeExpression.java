package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.Global;
import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.model.JsonNode;

public class JsonNodeExpression extends AbstractExpression {
    private final JsonNode node;
    private String jsonPath;

    public JsonNodeExpression(JsonNode node, Global global) {
        super(node.toJsonString(), global);
        this.node = node;
    }

    public JsonNodeExpression(JsonNode node, Global global, String jsonPath) {
        this(node, global);
        this.jsonPath = jsonPath;
    }

    @Override
    public <T> T getValue(Context inputContext) {
        return null;
    }

    @Override
    public Context get(Context inputContext) {
        return null;
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.NODE;
    }
}
