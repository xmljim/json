package io.xmljim.json.jsonpath.predicate.expression;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.variables.Global;
import io.xmljim.json.model.JsonNode;

import java.util.List;
import java.util.Optional;

class JsonNodeExpression extends AbstractExpression {
    private final JsonNode node;
    private String jsonPath;

    public JsonNodeExpression(JsonNode node, Global global) {
        super(node.toJsonString(), global);
        this.node = node;
        set(Context.create(node));
    }

    public JsonNodeExpression(JsonNode node, Global global, String jsonPath) {
        this(node, global);
        this.jsonPath = jsonPath;
    }

    @Override
    public Optional<Context> getContextAt(Context inputContext, int index) {
        return Optional.of(values().get(0));
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.NODE;
    }

    @Override
    public List<Context> values(Context inputContext) {
        return null;
    }
}
