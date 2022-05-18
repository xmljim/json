package io.xmljim.json.jsonpath;

import io.xmljim.json.factory.jsonpath.JsonPath;
import io.xmljim.json.factory.jsonpath.JsonPathBuilder;
import io.xmljim.json.jsonpath.predicate.expression.ExpressionFactory;
import io.xmljim.json.jsonpath.util.Settings;

class JsonPathBuilderImpl implements JsonPathBuilder {
    private final Settings settings = new Settings();

    @Override
    public JsonPath build() {
        return new JsonPathImpl(settings);
    }

    @Override
    public <T> JsonPathBuilder setVariable(String name, T value) {
        settings.setVariable(name, ExpressionFactory.create(value.toString(), settings));
        return this;
    }

    @Override
    public <T> JsonPathBuilder setProperty(String name, T value) {
        settings.setProperty(name, value);
        return this;
    }
}
