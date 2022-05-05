package io.xmljim.json.jsonpath;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.jsonpath.variables.Variables;

class JsonPathBuilderImpl implements JsonPathBuilder {
    private final Variables variables = new Variables();

    @Override
    public JsonPath build() {
        return new JsonPathImpl(variables);
    }

    @Override
    public <T> JsonPathBuilder setVariable(String name, T value) {
        variables.setVariable(name, ExpressionFactory.create(value.toString(), variables));
        return this;
    }

    @Override
    public <T> JsonPathBuilder setProperty(String name, T value) {
        variables.setProperty(name, value);
        return this;
    }
}
